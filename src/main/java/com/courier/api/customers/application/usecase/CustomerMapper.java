package com.courier.api.customers.application.usecase;

import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.domain.model.Customer;

/**
 * Maps customer domain objects to response DTOs.
 */
public final class CustomerMapper {

    private CustomerMapper() {
    }

    /**
     * Converts a customer domain object to a response DTO.
     *
     * @param customer the customer to convert
     * @return the customer response DTO
     */
    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getRole(),
                customer.isActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
