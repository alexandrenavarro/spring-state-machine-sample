package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;

import static com.github.anavarro.springstatemachinesample.workflow.WorkflowState.*;
import static com.github.anavarro.springstatemachinesample.workflow.WorkflowEvent.*;

/**
 * Created by anavarro on 26/07/16.
 */
@Configuration
@EnableStateMachineFactory
@Slf4j
public class WorkflowConfiguration extends EnumStateMachineConfigurerAdapter<WorkflowState, WorkflowEvent> {


    @Override
    public void configure(StateMachineConfigurationConfigurer<WorkflowState, WorkflowEvent> config) throws Exception {
        super.configure(config);
        config.withConfiguration().listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<WorkflowState, WorkflowEvent> states) throws Exception {
        super.configure(states);
        final WorkflowAction workflowAction = workflowAction();
        states
                .withStates()
                .initial(WORKFLOW_INITIATED_STATE)
                .state(WORKFLOW_INITIATED_STATE,  (stateContext) -> stateContext.getStateMachine().sendEvent(SAVE_RFQ_EVENT), null)
                .state(RFQ_SAVED_STATE, (stateContext) -> stateContext.getStateMachine().sendEvent(SAVE_PRICING_INFO_EVENT), null)
                .choice(PRICING_INFO_SAVED_STATE)
                .state(IS_A_BETA_USER_STATE)
                .state(IS_NOT_A_BETA_USER_STATE)
                .choice(PRICE_ASKED_STATE)
                .state(PRICE_ASK_CANCELLED_STATE)
                .state(PRICE_ASK_ACKED_STATE)
                .state(PRICING_OTHER_INFO_SAVED_STATE, (stateContext) -> stateContext.getStateMachine().sendEvent(APPLY_MARGIN_EVENT), null)
                .state(MARGIN_SAVED_STATE, (stateContext) -> stateContext.getStateMachine().sendEvent(DEFAULT_BOOKING_INFO_EVENT), null)
                .state(DEFAULT_BOOKING_INFO_SAVED_STATE, (stateContext) -> stateContext.getStateMachine().sendEvent(ASK_PDC_COMPUTATION_EVENT), null)
                .state(PDC_COMPUTATION_ASKED_STATE)
                .state(PDC_COMPUTATION_SAVED_STATE)
                .end(DEAL_SAVED_STATE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkflowState, WorkflowEvent> transitions) throws Exception {
        super.configure(transitions);
        final WorkflowAction workflowAction = workflowAction();
        transitions
                // Waiting request creation
                .withExternal()
                    .source(WORKFLOW_INITIATED_STATE)
                    .target(RFQ_SAVED_STATE)
                    .event(SAVE_RFQ_EVENT)
                    .action(workflowAction::saveRfq)
                    .and()
                .withExternal()
                    .source(RFQ_SAVED_STATE)
                    .target(PRICING_INFO_SAVED_STATE)
                    .event(SAVE_PRICING_INFO_EVENT)
                    .action(workflowAction::savePricingInfo)
                    .and()
                .withChoice()
                    .source(PRICING_INFO_SAVED_STATE)
                    .first(IS_A_BETA_USER_STATE, workflowAction::askIsABetaUser)
                    .last(IS_NOT_A_BETA_USER_STATE)
                    .and()
                .withExternal()
                    .source(IS_A_BETA_USER_STATE)
                    .target(PRICE_ASKED_STATE)
                    .action(workflowAction::askPrice)
                    .and()
                .withExternal()
                    .source(IS_NOT_A_BETA_USER_STATE)
                    .target(DEAL_SAVED_STATE)
                    .action(workflowAction::saveDeal)
                    .and()
                .withChoice()
                    .source(PRICE_ASKED_STATE)
                    .first(PRICE_ASK_ACKED_STATE, workflowAction::receiveAckPrice)
                    .last(PRICE_ASK_CANCELLED_STATE)
                    .and()
                // Waiting trader pricing info
                .withExternal()
                    .source(PRICE_ASK_ACKED_STATE)
                    .target(PRICING_OTHER_INFO_SAVED_STATE)
                    .event(RECEIVE_PRICE_EVENT)
                    .action(workflowAction::savePricingOtherInfo)
                    .and()
                .withExternal()
                    .source(PRICING_OTHER_INFO_SAVED_STATE)
                    .target(MARGIN_SAVED_STATE)
                    .event(APPLY_MARGIN_EVENT)
                    .action(workflowAction::applyMargin)
                    .and()
                .withExternal()
                    .source(MARGIN_SAVED_STATE)
                    .target(DEFAULT_BOOKING_INFO_SAVED_STATE)
                    .event(DEFAULT_BOOKING_INFO_EVENT)
                    .action(workflowAction::defaultBookingInfo)
                    .and()
                .withExternal()
                    .source(DEFAULT_BOOKING_INFO_SAVED_STATE)
                    .target(PDC_COMPUTATION_ASKED_STATE)
                    .event(ASK_PDC_COMPUTATION_EVENT)
                    .action(workflowAction::requestPdcComputation)
                    .and()
                // Waiting PDC result
                .withExternal()
                    .source(PDC_COMPUTATION_ASKED_STATE)
                    .target(DEAL_SAVED_STATE)
                    .event(RECEIVE_PDC_COMPUTATION_EVENT)
                    .action(workflowAction::saveDeal);
    }


    @Bean
    public StateMachineListener<WorkflowState, WorkflowEvent> listener() {
        return new WorkflowStateListener();
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public WorkflowController workflowController(final StateMachineFactory<WorkflowState, WorkflowEvent> stateMachineFactory) {
        return new WorkflowController(stateMachineFactory);
    }


    @Bean(name = StateMachineSystemConstants.TASK_EXECUTOR_BEAN_NAME)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        return taskExecutor;
    }

    @Bean
    public WorkflowAction workflowAction() {
        return new WorkflowAction();
    }

}
