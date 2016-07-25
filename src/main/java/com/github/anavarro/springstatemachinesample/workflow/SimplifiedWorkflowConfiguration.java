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
                    .initial(INITIATED_STATE)
                    .states(EnumSet.allOf(SimplifiedWorkflowState.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<SimplifiedWorkflowState, SimplifiedWorkflowEvent> transitions) throws Exception {
            super.configure(transitions);
            final SimplifiedWorkflowAction workflowAction = simplifiedWorkflowAction();

            // Use choiced in external to go from a state to another, is it better?
            transitions
                    .withChoice().source(INITIATED_STATE).first(RFQ_SAVED_STATE, workflowAction::saveRfqToRequest).last(INITIATED_STATE).and()
                    .withChoice().source(RFQ_SAVED_STATE).first(PRICING_INFO_SAVED_STATE, workflowAction::savePricingInfoToClientPricing).last(RFQ_SAVED_STATE).and()
                    .withChoice().source(PRICING_INFO_SAVED_STATE).first(BETA_USER_CONFIRMED_STATE, workflowAction::askIsBetaToUser).last(BETA_USER_DENIED_STATE).and()
                    .withChoice().source(BETA_USER_CONFIRMED_STATE).first(PRICE_ASKED_STATE, workflowAction::askPriceToTraderPrice).last(BETA_USER_CONFIRMED_STATE).and()
                    .withExternal().source(PRICE_ASKED_STATE).target(PRICE_RECEIVED_STATE).event(RECEIVING_PRICE_FROM_TRADER_PRICER_EVENT).and()
                    .withChoice().source(PRICE_RECEIVED_STATE).first(PRICING_OTHER_INFO_SAVED_STATE, workflowAction::savePricingOtherInfoToBooking).last(PRICE_RECEIVED_STATE).and()
                    .withChoice().source(PRICING_OTHER_INFO_SAVED_STATE).first(MARGIN_SAVED_STATE, workflowAction::applyMarginToClientPricing).last(PRICING_OTHER_INFO_SAVED_STATE).and()
                    .withChoice().source(MARGIN_SAVED_STATE).first(DEFAULT_BOOKING_INFO_SAVED_STATE, workflowAction::defaultBookingInfoToBooking).last(MARGIN_SAVED_STATE).and()
                    .withChoice().source(DEFAULT_BOOKING_INFO_SAVED_STATE).first(PDC_COMPUTATION_ASKED_STATE, workflowAction::askPdcComputationToRisky).last(DEFAULT_BOOKING_INFO_SAVED_STATE).and()
                    .withExternal().source(PDC_COMPUTATION_ASKED_STATE).target(PDC_COMPUTATION_SAVED_STATE).event(RECEIVING_PDC_COMPUTATION_FROM_RISKY_EVENT).and()
                    .withChoice().source(PDC_COMPUTATION_SAVED_STATE).first(TRANSACTION_SAVED_STATE, workflowAction::sendTransactionToRequest).last(PDC_COMPUTATION_SAVED_STATE);
        }


        @Bean
        public StateMachineListener<SimplifiedWorkflowState, SimplifiedWorkflowEvent> listener() {
            return new SimplifiedWorkflowStateListener();
        }

        @Bean
        public SimplifiedWorkflowController workflowController(final StateMachineFactory<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachineFactory) {
            return new SimplifiedWorkflowController(stateMachineFactory);
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
