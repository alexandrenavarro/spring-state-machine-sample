package com.github.anavarro.springstatemachinesample.workflow;

import lombok.Getter;

/**
 * Created by anavarro on 24/07/16.
 */
public enum SimplifiedWorkflowState {
    //INITIATED,
    //RFQ_STORAGE_SUCCEEDED,
    //PRICING_INFO_STORAGE_SUCCEEDED,
    //LIFE_SAVING_SUCCEEDED,
    //BETA_USER_CONFIRMED,
    //BETA_USER_DENIED,
    //OTC_QUOTE_REQUEST_ACCEPTED,
    //QUOTE_REQUEST_CANCELLED,
    //PRICING_ACK_INFO_SAVING_SUCEEDED,
    //OTC_QUOTED,
    //PRICING_OTHER_INFO_SAVING_SUCCEEDED,
    //MARGIN_RESPONSE_RECEIVED,
    //DEFAULT_BOOKING_INFO_REQUEST_SUCCEEDED,
    //PDC_CALCULATION_REQUEST_SUCCEEDED,
    //PDC_RESULT_REQUEST_SUCCEEDED,
    //TRANSACTION_SENT_ACKNOWLEDGED


    INITIATED_STATE(false),
    RFQ_SAVED_STATE(false),
    PRICING_INFO_SAVED_STATE(true), // NEW
    BETA_USER_CONFIRMED_STATE(false),
    BETA_USER_DENIED_STATE(false),
    PRICE_ASKED_STATE(true), // BEING_PRICED
    PRICE_RECEIVED_STATE(false),
    PRICING_OTHER_INFO_SAVED_STATE(false),
    MARGIN_SAVED_STATE(true), // PRICED
    DEFAULT_BOOKING_INFO_SAVED_STATE(false),
    PDC_COMPUTATION_ASKED_STATE(false),
    PDC_COMPUTATION_SAVED_STATE(false),
    TRANSACTION_SAVED_STATE(true); // DEAL_VALIDATED

    @Getter
    private boolean mandatoryToSendToClient;

    SimplifiedWorkflowState(final boolean aMandatoryToSendToClient) {
        this.mandatoryToSendToClient = aMandatoryToSendToClient;
    }


}
