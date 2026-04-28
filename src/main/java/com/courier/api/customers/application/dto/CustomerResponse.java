package com.courier.api.customers.application.dto;

import com.courier.api.customers.domain.model.CustomerRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String email,
        CustomerRole role,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
