package com.courier.api.customers.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.courier.api.customers.application.dto.CreateCustomerRequest;
import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.application.dto.UpdateCustomerRequest;
import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.exception.EmailAlreadyExistsException;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.model.CustomerRole;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import com.courier.api.customers.domain.ports.PasswordHasherPort;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerCommandUseCaseTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private PasswordHasherPort passwordHasher;

    private CustomerCommandUseCase customerCommandUseCase;

    @BeforeEach
    void setUp() {
        customerCommandUseCase = new CustomerCommandUseCase(customerRepository, passwordHasher);
    }

    @Test
    void create_ShouldSaveAndReturnCustomer_WhenEmailDoesNotExist() {
        CreateCustomerRequest request = new CreateCustomerRequest("John Doe", "john@example.com", "password", CustomerRole.SENDER);
        Customer customerToSave = Customer.newCustomer("John Doe", "john@example.com", "hashed_password", CustomerRole.SENDER);
        Customer savedCustomer = new Customer(UUID.randomUUID(), "John Doe", "john@example.com", "hashed_password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.password())).thenReturn("hashed_password");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponse response = customerCommandUseCase.create(request);

        assertNotNull(response);
        assertEquals(savedCustomer.getId(), response.id());
        assertEquals(savedCustomer.getName(), response.name());
        assertEquals(savedCustomer.getEmail(), response.email());
        assertEquals(savedCustomer.getRole(), response.role());
        assertTrue(response.active());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void create_ShouldThrowException_WhenEmailAlreadyExists() {
        CreateCustomerRequest request = new CreateCustomerRequest("John Doe", "john@example.com", "password", CustomerRole.SENDER);
        Customer existingCustomer = new Customer(UUID.randomUUID(), "John Doe", "john@example.com", "hashed_password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findByEmail(request.email())).thenReturn(Optional.of(existingCustomer));

        assertThrows(EmailAlreadyExistsException.class, () -> customerCommandUseCase.create(request));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void update_ShouldUpdateAndReturnCustomer_WhenCustomerExists() {
        UUID customerId = UUID.randomUUID();
        UpdateCustomerRequest request = new UpdateCustomerRequest("John Updated", CustomerRole.ADMIN);
        Customer existingCustomer = new Customer(customerId, "John Doe", "john@example.com", "hashed_password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());
        Customer updatedCustomer = new Customer(customerId, "John Updated", "john@example.com", "hashed_password", CustomerRole.ADMIN, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        CustomerResponse response = customerCommandUseCase.update(customerId, request);

        assertNotNull(response);
        assertEquals(customerId, response.id());
        assertEquals("John Updated", response.name());
        assertEquals(CustomerRole.ADMIN, response.role());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void update_ShouldThrowException_WhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();
        UpdateCustomerRequest request = new UpdateCustomerRequest("John Updated", CustomerRole.ADMIN);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerCommandUseCase.update(customerId, request));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deactivate_ShouldDeactivateCustomer_WhenCustomerExists() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "John Doe", "john@example.com", "hashed_password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());
        Customer deactivatedCustomer = new Customer(customerId, "John Doe", "john@example.com", "hashed_password", CustomerRole.SENDER, false, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(deactivatedCustomer);

        customerCommandUseCase.deactivate(customerId);

        verify(customerRepository).save(argThat(c -> !c.isActive()));
    }

    @Test
    void deactivate_ShouldThrowException_WhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerCommandUseCase.deactivate(customerId));
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
