package com.courier.api.customers.application.dto;

import com.courier.api.customers.domain.model.CustomerRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update a customer")
public record UpdateCustomerRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull CustomerRole role
) {
}
