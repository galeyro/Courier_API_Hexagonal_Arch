package com.courier.api.shared.events.domain.ports;

import com.courier.api.shared.events.domain.model.ShipmentEvent;

/**
 * Port for publishing domain events.
 */
public interface EventPublisher {

    /**
     * Publishes a shipment event to the specified topic.
     *
     * @param topic the topic to publish to
     * @param event the shipment event to publish
     */
    void publish(String topic, ShipmentEvent event);
}
