package com.courier.api.shared.events.infrastructure.out.messaging;

import com.courier.api.shared.events.domain.model.ShipmentEvent;
import com.courier.api.shared.events.domain.ports.EventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka implementation of the event publisher port.
 */
@Component
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new KafkaEventPublisher.
     *
     * @param kafkaTemplate the Kafka template for sending messages
     * @param objectMapper  the object mapper for serializing events
     */
    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes a shipment event to Kafka.
     *
     * @param topic the Kafka topic to publish to
     * @param event the shipment event to publish
     */
    @Override
    public void publish(String topic, ShipmentEvent event) {
        try {
            kafkaTemplate.send(topic, event.shipmentId().toString(), objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize shipment event", ex);
        }
    }
}
