package com.github.anavarro.springstatemachinesample.workflow;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by anavarro on 26/07/16.
 */
@AllArgsConstructor
public enum WorkflowEvent {

    SAVING_RFQ_EVENT(false),
    SAVING_PRICING_INFO_EVENT(true),
    ASKING_IS_BETA_USER_EVENT(true),
    RECEIVING_PRICE_EVENT(false),
    APPLYING_MARGIN_EVENT(true),
    DEFAULTING_BOOKING_INFO_EVENT(true),
    ASKING_PDC_COMPUTATION_EVENT(true),
    RECEIVING_PDC_COMPUTATION_EVENT(false);

    @Getter
    // is the event is sent internally via the action (true) or can be sent by the Controller (false)
    final boolean internalEvent;

}
