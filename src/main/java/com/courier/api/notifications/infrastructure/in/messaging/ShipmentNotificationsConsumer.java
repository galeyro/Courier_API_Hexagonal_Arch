package com.courier.api.notifications.infrastructure.in.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ShipmentNotificationsConsumer {

    private static final Logger log = LoggerFactory.getLogger(ShipmentNotificationsConsumer.class);

    private final ObjectMapper objectMapper;

    public ShipmentNotificationsConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = {"shipment.dispatched", "shipment.in_customs", "shipment.failed"},
            groupId = "notifications-consumer"
    )
    public void handleNotification(ConsumerRecord<String, String> record) {
        JsonNode payload = parse(record.value());
        log.info(
                "notification topic={} shipmentId={} senderId={} recipientId={} status={}",
                record.topic(),
                payload.path("shipmentId").asText(),
                payload.path("senderId").asText(),
                payload.path("recipientId").asText(),
                payload.path("status").asText()
        );
    }

    @KafkaListener(
            topics = {"shipment.dispatched", "shipment.in_customs", "shipment.failed"},
            groupId = "audit-consumer"
    )
    public void handleAudit(ConsumerRecord<String, String> record) {
        JsonNode payload = parse(record.value());
        log.info(
                "audit topic={} offset={} shipmentId={} timestamp={}",
                record.topic(),
                record.offset(),
                payload.path("shipmentId").asText(),
                payload.path("timestamp").asText()
        );
    }

    private JsonNode parse(String value) {
        try {
            return objectMapper.readTree(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse Kafka payload", ex);
        }
    }
}
