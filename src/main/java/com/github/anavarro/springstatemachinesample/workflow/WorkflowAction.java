package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;

import static com.github.anavarro.springstatemachinesample.workflow.WorkflowEvent.*;

/**
 * Created by anavarro on 24/07/16.
 */
@Slf4j
public final class WorkflowAction {

    public void storeRfq(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("storeRfq is starting ...");
        log.info("storeRfq finished.");
        stateContext.getStateMachine().sendEvent(STORING_PRICING_INFO);
    }

    public void storePricingInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("storePricingInfo is starting ...");
        log.info("storePricingInfo finished.");
        stateContext.getStateMachine().sendEvent(SAVING_STATUS_NEW);
    }

    public void saveStatusNew(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("saveStatusNew is starting ...");
        log.info("saveStatusNew finished.");
        stateContext.getStateMachine().sendEvent(REQUESTING_BETA_USER_INFO);
    }

    public boolean requestBetaUserInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("requestBetaUserInfo is starting ...");
        log.info("requestBetaUserInfo finished.");
        return true;
    }

    public boolean sendRfqToDefaultPricer(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("sendRfqToDefaultPricer is starting ...");
        log.info("sendRfqToDefaultPricer finished.");
        return true;
    }

    public boolean receivePrice(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("receivePrice is starting ...");
        log.info("receivePrice finished.");
        return true;
    }

    public void cancelRfq(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("cancelRfq is starting ...");
        log.info("cancelRfq finished.");
    }

    public void savePricingOtherInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("savePricingOtherInfo is starting ...");
        log.info("savePricingOtherInfo finished.");
        stateContext.getStateMachine().sendEvent(APPLYING_MARGIN);
    }

    public void applyMargin(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("applyMargin is starting ...");
        log.info("applyMargin finished.");
        stateContext.getStateMachine().sendEvent(DEFAULTING_BOOKING_INFO);
    }

    public void defaultBookingInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("defaultBookingInfo is starting ...");
        log.info("defaultBookingInfo finished.");
        stateContext.getStateMachine().sendEvent(REQUESTING_PDC_CALCULATION);
    }

    public void requestPdcCalculation(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("requestPdcCalculation is starting ...");
        log.info("requestPdcCalculation finished.");
        stateContext.getStateMachine().sendEvent(REQUESTING_PDC_CALCULATION);
    }

    public void sendTransactionToOnyxRequest(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("sendTransactionToOnyxRequest is starting ...");
        log.info("sendTransactionToOnyxRequest finished.");
    }

}
