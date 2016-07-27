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
    }

    public void savePricingInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("savePricingInfo is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("savePricingInfo finished.");
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

    public void savePricingOtherInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("savePricingOtherInfo is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("savePricingOtherInfo finished.");
    }

    public void applyMargin(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("applyMargin is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("applyMargin finished.");
    }

    public void defaultBookingInfo(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("defaultBookingInfo is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("defaultBookingInfo finished.");
    }

    public void requestPdcComputation(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("requestPdcComputation is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("requestPdcComputation finished.");
    }

    public void saveDeal(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.info("saveDeal is starting ... state:{}", stateContext.getStateMachine().getState().getId());
        log.info("saveDeal finished.");
    }

}
