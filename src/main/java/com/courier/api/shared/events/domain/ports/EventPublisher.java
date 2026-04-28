package com.courier.api.shared.events.domain.ports;

import com.courier.api.shared.events.domain.model.ShipmentEvent;

public interface EventPublisher {

    void publish(String topic, ShipmentEvent event);
}
