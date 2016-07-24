package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by anavarro on 23/07/16.
 */
@Slf4j
public class WorkflowStateListener
        extends StateMachineListenerAdapter<WorkflowState, WorkflowEvent> {

    @Override
    public void stateChanged(State<WorkflowState, WorkflowEvent> from, State<WorkflowState, WorkflowEvent> to) {
        log.debug("stateChange from:{}, to:{}", from, to);
    }

    @Override
    public void stateEntered(State<WorkflowState, WorkflowEvent> state) {
        log.debug("stateEntered state:{}", state);
    }

    @Override
    public void stateExited(State<WorkflowState, WorkflowEvent> state) {
        log.debug("stateExited state:{}", state);
    }

    @Override
    public void transition(Transition<WorkflowState, WorkflowEvent> transition) {
        log.debug("transition transition:{}", transition);
    }

    @Override
    public void transitionStarted(Transition<WorkflowState, WorkflowEvent> transition) {
        log.info("transitionStarted transition:{}", transition);
    }

    @Override
    public void transitionEnded(Transition<WorkflowState, WorkflowEvent> transition) {
        log.debug("transitionStarted transition:{}", transition);
    }

    @Override
    public void stateMachineStarted(StateMachine<WorkflowState, WorkflowEvent> stateMachine) {
        log.debug("stateMachineStarted stateMachine:{}", stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<WorkflowState, WorkflowEvent> stateMachine) {
        log.debug("stateMachineStopped stateMachine:{}", stateMachine);
    }

    @Override
    public void eventNotAccepted(Message<WorkflowEvent> event) {
        log.debug("eventNotAccepted event:{}", event);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        log.debug("extendedStateChanged key:{}, value={}", key, value);
    }

    @Override
    public void stateMachineError(StateMachine<WorkflowState, WorkflowEvent> stateMachine, Exception exception) {
        log.debug("stateMachineError stateMachine:{}, exception:{}", stateMachine, exception);
    }

    @Override
    public void stateContext(StateContext<WorkflowState, WorkflowEvent> stateContext) {
        log.debug("stateContext stateContext:{}", stateContext);
    }
}
