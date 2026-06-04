package com.courier.api.customers.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.model.CustomerRole;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerMapperTest {

    @Test
    void toResponse_ShouldMapAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Customer customer = new Customer(id, "John Doe", "john@example.com", "password", CustomerRole.SENDER, true, now, now);

        CustomerResponse response = CustomerMapper.toResponse(customer);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        assertEquals(CustomerRole.SENDER, response.role());
        assertTrue(response.active());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());
    }
}
