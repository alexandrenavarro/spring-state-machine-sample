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
public class SimplifiedWorkflowStateListener
        extends StateMachineListenerAdapter<SimplifiedWorkflowState, SimplifiedWorkflowEvent> {

    @Override
    public void stateChanged(State<SimplifiedWorkflowState, SimplifiedWorkflowEvent> from, State<SimplifiedWorkflowState, SimplifiedWorkflowEvent> to) {
        log.debug("stateChange from:{}, to:{}", from, to);
    }

    @Override
    public void stateEntered(State<SimplifiedWorkflowState, SimplifiedWorkflowEvent> state) {
        log.debug("stateEntered state:{}", state);
        if (state.getId().isMandatoryToSendToClient()) {
            log.info("Send to client");
        }
    }

    @Override
    public void stateExited(State<SimplifiedWorkflowState, SimplifiedWorkflowEvent> state) {
        log.debug("stateExited state:{}", state);
    }

    @Override
    public void transition(Transition<SimplifiedWorkflowState, SimplifiedWorkflowEvent> transition) {
        log.debug("transition transition:{}", transition);
    }

    @Override
    public void transitionStarted(Transition<SimplifiedWorkflowState, SimplifiedWorkflowEvent> transition) {
        log.info("transitionStarted transition:{}", transition);
    }

    @Override
    public void transitionEnded(Transition<SimplifiedWorkflowState, SimplifiedWorkflowEvent> transition) {
        log.debug("transitionStarted transition:{}", transition);
    }

    @Override
    public void stateMachineStarted(StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine) {
        log.debug("stateMachineStarted stateMachine:{}", stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine) {
        log.debug("stateMachineStopped stateMachine:{}", stateMachine);
    }

    @Override
    public void eventNotAccepted(Message<SimplifiedWorkflowEvent> event) {
        log.debug("eventNotAccepted event:{}", event);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        log.debug("extendedStateChanged key:{}, value={}", key, value);
    }

    @Override
    public void stateMachineError(StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine, Exception exception) {
        log.debug("stateMachineError stateMachine:{}, exception:{}", stateMachine, exception);
    }

    @Override
    public void stateContext(StateContext<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateContext) {
        log.debug("stateContext stateContext:{}", stateContext);
    }
}
