package com.github.anavarro.springstatemachinesample.workflow;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.http.HttpStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            Awaitility.await().until(() -> stateMachine != null && stateMachine.getState() != null);
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
            // TODO create a ResourceNotFoundException and map in spring
            throw new IllegalArgumentException("WorkflowId not found");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{workflowId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a workflow instance by passing a id")
    @ApiResponses({@ApiResponse(code = 204, message = "Workflow was deleted")})
    public void delete(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId) {
        if (stateMachineMap.containsKey(workflowId)) {
            stateMachineMap.get(workflowId).stop();
            stateMachineMap.remove(workflowId);
        } else {
            // TODO create a specific and map in spring
            throw new IllegalArgumentException("Workflow already created");
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{workflowId}/events")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Launch a event on a workflow")
    @ApiResponses({@ApiResponse(code = 200, message = "Workflow was created"),
                   @ApiResponse(code = 400, message = "Bad request, workflowEventId is valid?"),
                   @ApiResponse(code = 404, message = "WorkflowId not found")})
    public WorkflowEvent triggerWorflowEvent(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId,
                                             @RequestParam("workflowEventId") @ApiParam(value = "workflowEventId", required = true) final WorkflowEvent workflowEventId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null) {
            if (stateMachine.getTransitions().stream().anyMatch(workflowStateWorkflowEventTransition -> workflowStateWorkflowEventTransition.getSource().getId() == stateMachine.getState().getId() && workflowStateWorkflowEventTransition.getTrigger().getEvent() == workflowEventId)) {
                final boolean result = this.stateMachineMap.get(workflowId).sendEvent(workflowEventId);
                if (result) {
                    return workflowEventId;
                } else {
                    throw new IllegalArgumentException("Impossible to sendEvent.");
                }
            } else {
                throw new IllegalArgumentException("The workflowEventId:" + workflowEventId + " is not launcheable when state:" + stateMachine.getState().getId()+ ", so it is ignored.");
            }
        } else {
            // TODO create a ResourceNotFoundException and map in spring
            throw new IllegalArgumentException("WorkflowId not found");
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{workflowId}/states")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve a workflow state instance by passing a id")
    @ApiResponses({@ApiResponse(code = 200, message = "Workflow was created"),
            @ApiResponse(code = 404, message = "WorkflowId not found")})
    public WorkflowState getState(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null) {
            return stateMachine.getState().getId();
        } else {
            // TODO create a ResourceNotFoundException and map in spring
            throw new IllegalArgumentException("WorkflowId not found");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{workflowId}/states/next")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve a workflow state instance by passing a id")
    @ApiResponses({@ApiResponse(code = 200, message = "Workflow was created"),
            @ApiResponse(code = 404, message = "WorkflowId not found")})
    public List<WorkflowState> getNextStates(@PathVariable("workflowId") @ApiParam(value = "workflowId", required = true) final String workflowId) {
        final StateMachine<WorkflowState, WorkflowEvent> stateMachine = this.stateMachineMap.get(workflowId);
        if (stateMachine != null) {
            return stateMachine.getTransitions().stream().filter(workflowStateWorkflowEventTransition -> workflowStateWorkflowEventTransition.getSource().getId() == stateMachine.getState().getId()).map(workflowStateWorkflowEventTransition -> workflowStateWorkflowEventTransition.getTarget().getId()).collect(Collectors.toList());
        } else {
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
