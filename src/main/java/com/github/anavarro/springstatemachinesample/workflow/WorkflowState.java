package com.github.anavarro.springstatemachinesample.workflow;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by anavarro on 26/07/16.
 */
@AllArgsConstructor
public enum WorkflowState {

    WORKFLOW_INITIATED_STATE(false, "PRE_NEW"),
    RFQ_SAVED_STATE(false, "PRE_NEW"),
    PRICING_INFO_SAVED_STATE(true, "NEW"),
    IS_A_BETA_USER_STATE(false, "PRE_BEING_PRICE"),
    IS_NOT_A_BETA_USER_STATE(false, "PRE_BEING_PRICE"),
    PRICE_ASKED_STATE(true, "BEING_PRICE"),
    PRICE_ASK_CANCELLED_STATE(false, "PRE_PRICED"),
    PRICE_ASK_ACKED_STATE(false, "PRE_PRICED"),
    PRICING_OTHER_INFO_SAVED_STATE(true, "PRICED"),
    MARGIN_SAVED_STATE(false, "PRE_DEAL_VALIDATED"),
    DEFAULT_BOOKING_INFO_SAVED_STATE(false, "PRE_DEAL_VALIDATED"),
    PDC_COMPUTATION_ASKED_STATE(false, "PRE_DEAL_VALIDATED"),
    PDC_COMPUTATION_SAVED_STATE(false, "PRE_DEAL_VALIDATED"),
    DEAL_SAVED_STATE(true, "PRE_DEAL_VALIDATED");
    // TODO add the other states

    @Getter
    private boolean mandatoryToSendToClient;
    @Getter
    private String functionalState;


}
