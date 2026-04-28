package com.courier.api.shared.events.domain.model;

public final class EventTopics {

    public static final String SHIPMENT_DISPATCHED = "shipment.dispatched";
    public static final String SHIPMENT_IN_CUSTOMS = "shipment.in_customs";
    public static final String SHIPMENT_FAILED = "shipment.failed";

    private EventTopics() {
    }
}
