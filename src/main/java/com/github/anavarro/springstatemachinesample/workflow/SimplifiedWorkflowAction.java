package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;

/**
 * Created by anavarro on 24/07/16.
 */
@Slf4j
public class SimplifiedWorkflowAction {


    public boolean saveRfqToRequest(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("saveRfqToRequest is starting ...");
        log.info("saveRfqToRequest finished.");
        return true;
    }

    public boolean savePricingInfoToClientPricing(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("savePricingInfoToClientPricing is starting ...");
        log.info("savePricingInfoToClientPricing finished.");
        return true;
    }

    public boolean askIsBetaToUser(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("askIsBetaToUser is starting ...");
        log.info("askIsBetaToUser finished.");
        return true;
    }

    public boolean askPriceToTraderPrice(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("askPriceToTraderPrice is starting ...");
        log.info("askPriceToTraderPrice finished.");
        return true;
    }

    public boolean savePricingOtherInfoToBooking(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("savePricingOtherInfoToBooking is starting ...");
        log.info("savePricingOtherInfoToBooking finished.");
        return true;
    }

    public boolean applyMarginToClientPricing(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("applyMarginToClientPricing is starting ...");
        log.info("applyMarginToClientPricing finished.");
        return true;
    }

    public boolean defaultBookingInfoToBooking(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("defaultBookingInfo is starting ...");
        log.info("defaultBookingInfo finished.");
        return true;
    }

    public boolean askPdcComputationToRisky(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("askPdcComputationToRisky is starting ...");
        log.info("askPdcComputationToRisky finished.");
        return true;
    }

    public boolean sendTransactionToRequest(final StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.info("sendTransactionToRequest is starting ...");
        log.info("sendTransactionToRequest finished.");
        return true;
    }


}
