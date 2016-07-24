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


    INITIATED(false),
    RFQ_SAVED(false),
    PRICING_INFO_SAVED(true), // NEW
    BETA_USER_CONFIRMED(false),
    BETA_USER_DENIED(false),
    PRICE_ASKED(true), // BEING_PRICED
    PRICE_RECEIVED(false),
    PRICING_OTHER_INFO_SAVED(false),
    MARGIN_SAVED(true), // PRICED
    DEFAULT_BOOKING_INFO_SAVED(false),
    PDC_COMPUTATION_ASKED(false),
    PDC_COMPUTATION_SAVED(false),
    TRANSACTION_SAVED(true); // DEAL_VALIDATED

    @Getter
    private boolean mandatoryToSendToClient;

    SimplifiedWorkflowState(final boolean aMandatoryToSendToClient) {
        this.mandatoryToSendToClient = aMandatoryToSendToClient;
    }


}
