package com.github.anavarro.springstatemachinesample.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.statemachine.StateMachine;

/**
 * Created by anavarro on 23/07/16.
 */

@Getter
@Setter
@ToString
@Builder
public class Workflow {

    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("workflowState")
    private WorkflowState workflowState;
}
