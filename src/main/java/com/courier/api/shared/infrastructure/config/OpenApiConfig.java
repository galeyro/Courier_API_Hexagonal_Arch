package com.courier.api.shared.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Courier API",
                version = "1.0.0",
                description = "Courier API with hexagonal architecture, shipping strategies and Kafka-based events"
        )
)
public class OpenApiConfig {
}
