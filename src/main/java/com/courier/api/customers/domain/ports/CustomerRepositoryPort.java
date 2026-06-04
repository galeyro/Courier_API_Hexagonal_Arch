package com.courier.api.customers.domain.ports;

import com.courier.api.customers.domain.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port for customer persistence operations.
 */
public interface CustomerRepositoryPort {

    /**
     * Saves a customer.
     *
     * @param customer the customer to save
     * @return the saved customer
     */
    Customer save(Customer customer);

    /**
     * Finds a customer by ID.
     *
     * @param id the customer ID
     * @return an optional containing the customer, or empty if not found
     */
    Optional<Customer> findById(UUID id);

    /**
     * Finds an active customer by ID.
     *
     * @param id the customer ID
     * @return an optional containing the customer, or empty if not found or inactive
     */
    Optional<Customer> findActiveById(UUID id);

    /**
     * Finds a customer by email.
     *
     * @param email the customer email
     * @return an optional containing the customer, or empty if not found
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Returns all customers.
     *
     * @return list of customers
     */
    List<Customer> findAll();
}
