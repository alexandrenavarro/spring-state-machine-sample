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
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by anavarro on 23/07/16.
 */
@Controller
@Slf4j
public class WorkflowController {

    final private Map<String, StateMachine<WorkflowState, WorkflowEvent>> stateMachineMap = new ConcurrentHashMap<>();
    final private StateMachineFactory<WorkflowState, WorkflowEvent> stateMachineFactory;


    public WorkflowController(final StateMachineFactory<WorkflowState, WorkflowEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/workflows")
    @ResponseBody
    public Collection<Workflow> getWorkflows() {
        return this.stateMachineMap.values().stream().map(stateMachine -> {
            return mapStateMachineToWorkflow(stateMachine);
        }).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/workflows")
    @ResponseBody
    public Workflow postWorflow() {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.start();
        this.stateMachineMap.put(stateMachine.getUuid().toString(), stateMachine);
        log.debug("stateMachine:{}", stateMachine);
        // Caution State is set to initialState not current state because it is not yet done depending of ThreadPool
        return Workflow.builder().workflowId(stateMachine.getUuid().toString()).workflowState(stateMachine.getInitialState().getId()).build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/workflows/{workflowId}")
    @ResponseBody
    public Workflow getWorkflow(@PathVariable final String workflowId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null ) {
            return mapStateMachineToWorkflow(stateMachine);
        } else {
            // TODO Better way to manage with a NOT FOUND Error
            return Workflow.builder().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/workflows/{workflowId}/events/{workflowEventId}")
    @ResponseBody
    // TODO maybe change the return type (it is a POST command)
    public WorkflowEvent postWorkflowEvent(@PathVariable final String workflowId, @PathVariable final WorkflowEvent workflowEventId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
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

    private Workflow mapStateMachineToWorkflow(final StateMachine<WorkflowState, WorkflowEvent> stateMachine) {
        return Workflow.builder().workflowId(stateMachine.getUuid().toString()).workflowState(stateMachine.getState().getId()).build();
    }


}
