package com.courier.api.customers.infrastructure.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for customer entities.
 */
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

    /**
     * Finds a customer entity by email.
     *
     * @param email the email to search for
     * @return an optional containing the customer entity, or empty if not found
     */
    Optional<CustomerEntity> findByEmail(String email);

    /**
     * Finds an active customer entity by ID.
     *
     * @param id the customer ID to search for
     * @return an optional containing the active customer entity, or empty if not found
     */
    Optional<CustomerEntity> findByIdAndActiveTrue(UUID id);
}
