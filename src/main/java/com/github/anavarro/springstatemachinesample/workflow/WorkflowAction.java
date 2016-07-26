package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;

import static com.github.anavarro.springstatemachinesample.workflow.WorkflowEvent.*;

/**
 * Created by anavarro on 26/07/16.
 */
@Slf4j
public final class WorkflowAction {

    public void saveRfq(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("saveRfq is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("saveRfq finished.");
        stateContext.getStateMachine().sendEvent(SAVING_PRICING_INFO_EVENT);
    }

    public void savePricingInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("savePricingInfo is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("savePricingInfo finished.");
        stateContext.getStateMachine().sendEvent(ASKING_IS_BETA_USER_EVENT);
    }

    public boolean askIsABetaUser(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("askIsABetaUser is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("askIsABetaUser finished.");
        final boolean isABetaUser = true;
        return isABetaUser;
    }

    public void askPrice(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
         log.info("askPrice is starting ... state:{}", stateContext.getStateMachine().getState().getId());
         log.info("askPrice finished.");
    }

    public boolean receiveAckPrice(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("receiveAckPrice is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("receiveAckPrice finished.");
        return true;
    }

    public void cancelRfq(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("cancelRfq is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("cancelRfq finished.");
    }

    public void savePricingOtherInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("savePricingOtherInfo is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("savePricingOtherInfo finished.");
        stateContext.getStateMachine().sendEvent(APPLYING_MARGIN_EVENT);
    }

    public void applyMargin(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("applyMargin is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("applyMargin finished.");
        stateContext.getStateMachine().sendEvent(DEFAULTING_BOOKING_INFO_EVENT);
    }

    public void defaultBookingInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("defaultBookingInfo is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("defaultBookingInfo finished.");
        stateContext.getStateMachine().sendEvent(ASKING_PDC_COMPUTATION_EVENT);
    }

    public void requestPdcComputation(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("requestPdcComputation is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("requestPdcComputation finished.");
    }

    public void sendTransaction(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("sendTransaction is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("sendTransaction finished.");
    }

}
