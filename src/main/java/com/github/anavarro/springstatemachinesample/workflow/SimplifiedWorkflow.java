package com.github.anavarro.springstatemachinesample.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by anavarro on 23/07/16.
 */

@Getter
@Setter
@ToString
@Builder
public class SimplifiedWorkflow {

    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("workflowState")
    private SimplifiedWorkflowState workflowState;
}
