package com.github.anavarro.springstatemachinesample.workflow;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.http.HttpStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by anavarro on 26/07/16.
 */
@RestController
@Slf4j
@Api(tags = {"Workflows"}, description = "Manage workflow instances")
@RequestMapping(value = "/workflows")
public final class WorkflowController {

    final private Map<String, StateMachine<WorkflowState, WorkflowEvent>> stateMachineMap = new ConcurrentHashMap<>();
    final private StateMachineFactory<WorkflowState, WorkflowEvent> stateMachineFactory;

    public WorkflowController(final StateMachineFactory<WorkflowState, WorkflowEvent> aStateMachineFactory) {
        this.stateMachineFactory = aStateMachineFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve all workflows instances")
    @ApiResponses({@ApiResponse(code = 200, message = "workflows were found")})
    public Iterable<Workflow> findAll() {
        return this.stateMachineMap.values().stream().map(stateMachine -> {
            return mapStateMachineToWorkflow(stateMachine);
        }).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{workflowId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a workflow instance by passing a id")
    @ApiResponses({@ApiResponse(code = 201, message = "Workflow was created"),
                   @ApiResponse(code = 409, message = "Workflow already created")})
    public Workflow save(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId) {
        if (!stateMachineMap.containsKey(workflowId)) {
            final StateMachine<WorkflowState, WorkflowEvent>
                    stateMachine =
                    stateMachineFactory.getStateMachine(workflowId);
            stateMachine.start();
            this.stateMachineMap.put(stateMachine.getId(), stateMachine);
            log.debug("stateMachine:{}", stateMachine);
            // Need because ThreadPool is used
            Awaitility.await().until(() -> stateMachine != null);
            stateMachine.sendEvent(WorkflowEvent.SAVING_RFQ_EVENT);
            Awaitility.await().until(() -> stateMachine.getState() != null);
            return Workflow.builder()
                           .workflowId(stateMachine.getId())
                           .workflowState(stateMachine.getState().getId())
                           .build();
        } else {
            // TODO create a specific and map in spring
            throw new IllegalArgumentException("Workflow already created");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{workflowId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve a workflow instance by passing a id")
    @ApiResponses({@ApiResponse(code = 200, message = "Workflow was created"),
                   @ApiResponse(code = 404, message = "WorkflowId not found")})
    public Workflow findOne(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null) {
            return mapStateMachineToWorkflow(stateMachine);
        } else {
            // TODO create a ResourceNotFoundException and map in spring
            throw new IllegalArgumentException("WorkflowId not found");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{workflowId}/events/{workflowEventId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Launch a event on a workflow")
    @ApiResponses({@ApiResponse(code = 200, message = "Workflow was created"),
                   @ApiResponse(code = 400, message = "Bad request, workflowEventId is valid?"),
                   @ApiResponse(code = 404, message = "WorkflowId not found")})
    public WorkflowEvent saveWorkflowEvent(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId,
                                           @PathVariable("workflowEventId") @ApiParam(value = "workflowEventId", required = true) final WorkflowEvent workflowEventId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null) {
            final boolean result = this.stateMachineMap.get(workflowId).sendEvent(workflowEventId);
            if (result) {
                return workflowEventId;
            } else {
                //// TODO create a ResourceNotFoundException and map in spring
                throw new IllegalArgumentException("Impossible to sendEvent");
            }
        } else {
            log.warn("WorflowId:{} is not found, worflowEventId={} will not be sent.", workflowId, workflowEventId);
            // TODO create a ResourceNotFoundException and map in spring
            throw new IllegalArgumentException("WorkflowId not found");
        }

    }

    private Workflow mapStateMachineToWorkflow(final StateMachine<WorkflowState, WorkflowEvent> stateMachine) {
        return Workflow.builder()
                       .workflowId(stateMachine.getUuid().toString())
                       .workflowState(stateMachine.getState().getId())
                       .build();
    }

}
