package com.github.anavarro.springstatemachinesample.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by anavarro on 23/07/16.
 */
@Controller
@Slf4j
public class SimplifiedWorkflowController {

    final private Map<String, StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent>> stateMachineMap = new ConcurrentHashMap<>();
    final private StateMachineFactory<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachineFactory;


    public SimplifiedWorkflowController(final StateMachineFactory<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/workflows")
    @ResponseBody
    public Collection<SimplifiedWorkflow> getSimplifiedWorkflows() {
        return this.stateMachineMap.values().stream().map(stateMachine -> {
            return mapStateMachineToSimplifiedWorkflow(stateMachine);
        }).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/workflows")
    @ResponseBody
    public SimplifiedWorkflow postWorflow() {
        final StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.start();
        this.stateMachineMap.put(stateMachine.getUuid().toString(), stateMachine);
        log.debug("stateMachine:{}", stateMachine);
        // Caution State is set to initialState not current state because it is not yet done depending of ThreadPool
        return SimplifiedWorkflow.builder().workflowId(stateMachine.getUuid().toString()).workflowState(stateMachine.getInitialState().getId()).build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/workflows/{workflowId}")
    @ResponseBody
    public SimplifiedWorkflow getWorkflow(@PathVariable final String workflowId) {
        final StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null ) {
            return mapStateMachineToSimplifiedWorkflow(stateMachine);
        } else {
            // TODO Better way to manage with a NOT FOUND Error
            return SimplifiedWorkflow.builder().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/workflows/{workflowId}/events/{workflowEventId}")
    @ResponseBody
    // TODO maybe change the return type (it is a POST command)
    public SimplifiedWorkflowEvent postWorkflowEvent(@PathVariable final String workflowId, @PathVariable final SimplifiedWorkflowEvent workflowEventId) {
        final StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null) {
            this.stateMachineMap.get(workflowId).sendEvent(MessageBuilder
                    .withPayload(workflowEventId)
                    .setHeader("eventHeader", workflowEventId.name())
                    .build());

        } else {
            // TODO
            log.warn("WorflowId:{} is not found, worflowEventId={} will not be sent.", workflowId, workflowEventId);
        }
        return workflowEventId;
    }

    private SimplifiedWorkflow mapStateMachineToSimplifiedWorkflow(final StateMachine<SimplifiedWorkflowState, SimplifiedWorkflowEvent> stateMachine) {
        return SimplifiedWorkflow.builder().workflowId(stateMachine.getUuid().toString()).workflowState(stateMachine.getState().getId()).build();
    }


}
