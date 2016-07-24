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

import static com.github.anavarro.springstatemachinesample.workflow.SimplifiedWorkflowEvent.*;
import static com.github.anavarro.springstatemachinesample.workflow.SimplifiedWorkflowState.*;

/**
 * Created by anavarro on 24/07/16.
 */
@Configuration
@EnableStateMachineFactory
@Slf4j
public class SimplifiedWorkflowConfiguration extends EnumStateMachineConfigurerAdapter<SimplifiedWorkflowState, SimplifiedWorkflowEvent> {


        @Override
        public void configure(StateMachineConfigurationConfigurer<SimplifiedWorkflowState, SimplifiedWorkflowEvent> config) throws Exception {
            super.configure(config);
            config.withConfiguration().listener(listener());

        }

        @Override
        public void configure(StateMachineStateConfigurer<SimplifiedWorkflowState, SimplifiedWorkflowEvent> states) throws Exception {
            super.configure(states);
            states
                    .withStates()
                    .initial(INITIATED)
                    .states(EnumSet.allOf(SimplifiedWorkflowState.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<SimplifiedWorkflowState, SimplifiedWorkflowEvent> transitions) throws Exception {
            super.configure(transitions);
            final SimplifiedWorkflowAction workflowAction = simplifiedWorkflowAction();

            // Use choiced in external to go from a state to another, is it better?
            transitions
                    .withChoice().source(INITIATED).first(RFQ_SAVED, workflowAction::saveRfqToRequest).last(INITIATED).and()
                    .withChoice().source(RFQ_SAVED).first(PRICING_INFO_SAVED, workflowAction::savePricingInfoToClientPricing).last(RFQ_SAVED).and()
                    .withChoice().source(PRICING_INFO_SAVED).first(BETA_USER_CONFIRMED, workflowAction::askIsBetaToUser).last(BETA_USER_DENIED).and()
                    .withChoice().source(BETA_USER_CONFIRMED).first(PRICE_ASKED, workflowAction::askPriceToTraderPrice).last(BETA_USER_CONFIRMED).and()
                    .withExternal().source(PRICE_ASKED).target(PRICE_RECEIVED).event(RECEIVING_PRICE_FROM_TRADER_PRICER).and()
                    .withChoice().source(PRICE_RECEIVED).first(PRICING_OTHER_INFO_SAVED, workflowAction::savePricingOtherInfoToBooking).last(PRICE_RECEIVED).and()
                    .withChoice().source(PRICING_OTHER_INFO_SAVED).first(MARGIN_SAVED, workflowAction::applyMarginToClientPricing).last(PRICING_OTHER_INFO_SAVED).and()
                    .withChoice().source(MARGIN_SAVED).first(DEFAULT_BOOKING_INFO_SAVED, workflowAction::defaultBookingInfoToBooking).last(MARGIN_SAVED).and()
                    .withChoice().source(DEFAULT_BOOKING_INFO_SAVED).first(PDC_COMPUTATION_ASKED, workflowAction::askPdcComputationToRisky).last(DEFAULT_BOOKING_INFO_SAVED).and()
                    .withExternal().source(PDC_COMPUTATION_ASKED).target(PDC_COMPUTATION_SAVED).event(RECEIVING_PDC_COMPUTATION_FROM_RISKY).and()
                    .withChoice().source(PDC_COMPUTATION_SAVED).first(TRANSACTION_SAVED, workflowAction::sendTransactionToRequest).last(PDC_COMPUTATION_SAVED);
        }


        @Bean
        public StateMachineListener<SimplifiedWorkflowState, SimplifiedWorkflowEvent> listener() {
            return new SimplifiedWorkflowStateListener();
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
        public SimplifiedWorkflowAction simplifiedWorkflowAction() {
            return new SimplifiedWorkflowAction();
        }

}
