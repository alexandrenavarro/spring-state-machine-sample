package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;



/**
 * Created by anavarro on 26/07/16.
 */
@Slf4j
public final class WorkflowStateListener
        extends StateMachineListenerAdapter<WorkflowState, WorkflowEvent> {

    @Override
    public void stateChanged(final State<WorkflowState, WorkflowEvent> from, final State<WorkflowState, WorkflowEvent> to) {
        log.debug("stateChange from:{}, to:{}", from, to);
    }

    @Override
    public void stateEntered(final State<WorkflowState, WorkflowEvent> state) {
        log.debug("stateEntered state:{}", state);
        if (state.getId().isMandatoryToSendToClient()) {
            log.debug("send to client");
        }
    }

    @Override
    public void stateExited(final State<WorkflowState, WorkflowEvent> state) {
        log.debug("stateExited state:{}", state);
    }

    @Override
    public void transition(final Transition<WorkflowState, WorkflowEvent> transition) {
        log.debug("transition transition:{}", transition);
    }

    @Override
    public void transitionStarted(final Transition<WorkflowState, WorkflowEvent> transition) {
        log.info("transitionStarted transition:{}", transition);
    }

    @Override
    public void transitionEnded(final Transition<WorkflowState, WorkflowEvent> transition) {
        log.debug("transitionStarted transition:{}", transition);
    }

    @Override
    public void stateMachineStarted(final StateMachine<WorkflowState, WorkflowEvent> stateMachine) {
        log.debug("stateMachineStarted stateMachine:{}", stateMachine);
    }

    @Override
    public void stateMachineStopped(final StateMachine<WorkflowState, WorkflowEvent> stateMachine) {
        log.debug("stateMachineStopped stateMachine:{}", stateMachine);
    }

    @Override
    public void eventNotAccepted(final Message<WorkflowEvent> event) {
        log.debug("eventNotAccepted event:{}", event);
    }

    @Override
    public void extendedStateChanged(final Object key, final Object value) {
        log.debug("extendedStateChanged key:{}, value={}", key, value);
    }

    @Override
    public void stateMachineError(final StateMachine<WorkflowState, WorkflowEvent> stateMachine, final Exception exception) {
        log.debug("stateMachineError stateMachine:{}, exception:{}", stateMachine, exception);
    }

    @Override
    public void stateContext(final StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.debug("stateContext stateContext:{}", stateContext);
    }
}
