package com.github.anavarro.springstatemachinesample.workflow;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by anavarro on 26/07/16.
 */
@AllArgsConstructor
public enum WorkflowEvent {

    SAVE_RFQ_EVENT(true),
    SAVE_PRICING_INFO_EVENT(true),
    ASK_IS_A_BETA_USER_EVENT(true),
    RECEIVE_PRICE_EVENT(false),
    APPLY_MARGIN_EVENT(true),
    DEFAULT_BOOKING_INFO_EVENT(true),
    ASK_PDC_COMPUTATION_EVENT(true),
    RECEIVE_PDC_COMPUTATION_EVENT(false);

    @Getter
    // is the event is sent internally via the action (true) or can be sent by the Controller (false)
    final boolean internalEvent;

}
