package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;

import java.util.EnumSet;

import static com.github.anavarro.springstatemachinesample.workflow.WorkflowState.*;
import static com.github.anavarro.springstatemachinesample.workflow.WorkflowEvent.*;

/**
 * Created by anavarro on 23/07/16.
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
        states
                .withStates()
                .initial(INITIATED)
                .states(EnumSet.allOf(WorkflowState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkflowState, WorkflowEvent> transitions) throws Exception {
        super.configure(transitions);
        final WorkflowAction workflowAction = workflowAction();
        transitions
                .withExternal().source(INITIATED).target(RFQ_STORAGE_SUCCEEDED).event(STORING_RFQ).action(workflowAction::storeRfq).and()
                .withExternal().source(RFQ_STORAGE_SUCCEEDED).target(PRICING_INFO_STORAGE_SUCCEEDED).event(STORING_PRICING_INFO).action(workflowAction::storeRfq).and()
                .withExternal().source(PRICING_INFO_STORAGE_SUCCEEDED).target(LIFE_SAVING_SUCCEEDED).event(SAVING_STATUS_NEW).action(workflowAction::saveStatusNew).and()
                .withChoice().source(LIFE_SAVING_SUCCEEDED).first(BETA_USER_CONFIRMED, workflowAction::requestBetaUserInfo).last(BETA_USER_DENIED).and()
                .withChoice().source(BETA_USER_CONFIRMED).first(OTC_QUOTE_REQUEST_ACCEPTED, workflowAction::sendRfqToDefaultPricer).last(QUOTE_REQUEST_CANCELLED).and()
                // Check if it is really that
                .withChoice().source(OTC_QUOTE_REQUEST_ACCEPTED).first(OTC_QUOTED, workflowAction::receivePrice).last(QUOTE_REQUEST_CANCELLED).and()
                .withExternal().source(OTC_QUOTED).target(PRICING_OTHER_INFO_SAVING_SUCCEEDED).action(workflowAction::savePricingOtherInfo).and()
                .withExternal().source(PRICING_OTHER_INFO_SAVING_SUCCEEDED).target(MARGIN_RESPONSE_RECEIVED).action(workflowAction::applyMargin).and()
                .withExternal().source(MARGIN_RESPONSE_RECEIVED).target(DEFAULT_BOOKING_INFO_REQUEST_SUCCEEDED).action(workflowAction::defaultBookingInfo).and()
                .withExternal().source(DEFAULT_BOOKING_INFO_REQUEST_SUCCEEDED).target(PDC_CALCULATION_REQUEST_SUCCEEDED).action(workflowAction::requestPdcCalculation).and()
                .withExternal().source(PDC_CALCULATION_REQUEST_SUCCEEDED).target(TRANSACTION_SENT_ACKNOWLEDGED).action(workflowAction::requestPdcCalculation);
    }


    @Bean
    public StateMachineListener<WorkflowState, WorkflowEvent> listener() {
        return new WorkflowStateListener();
    }

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
