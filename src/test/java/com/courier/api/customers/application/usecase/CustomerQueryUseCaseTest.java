package com.courier.api.customers.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.model.CustomerRole;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerQueryUseCaseTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    private CustomerQueryUseCase customerQueryUseCase;

    @BeforeEach
    void setUp() {
        customerQueryUseCase = new CustomerQueryUseCase(customerRepository);
    }

    @Test
    void findAll_ShouldReturnListOfCustomers() {
        Customer customer1 = new Customer(UUID.randomUUID(), "John Doe", "john@example.com", "password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());
        Customer customer2 = new Customer(UUID.randomUUID(), "Jane Doe", "jane@example.com", "password", CustomerRole.ADMIN, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<CustomerResponse> responses = customerQueryUseCase.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("john@example.com", responses.get(0).email());
        assertEquals("jane@example.com", responses.get(1).email());
    }

    @Test
    void findById_ShouldReturnCustomer_WhenCustomerExists() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "John Doe", "john@example.com", "password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerQueryUseCase.findById(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.id());
        assertEquals("john@example.com", response.email());
    }

    @Test
    void findById_ShouldThrowException_WhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerQueryUseCase.findById(customerId));
    }
}
