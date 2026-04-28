package com.courier.api.shipments.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic shipmentDispatchedTopic() {
        return TopicBuilder.name("shipment.dispatched").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic shipmentInCustomsTopic() {
        return TopicBuilder.name("shipment.in_customs").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic shipmentFailedTopic() {
        return TopicBuilder.name("shipment.failed").partitions(1).replicas(1).build();
    }
}
