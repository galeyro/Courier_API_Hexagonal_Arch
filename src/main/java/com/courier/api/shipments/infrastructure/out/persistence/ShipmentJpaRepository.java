package com.courier.api.shipments.infrastructure.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for shipment entities.
 */
public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, UUID> {

    /**
     * Finds shipments by sender or recipient ID.
     */
    List<ShipmentEntity> findBySenderIdOrRecipientIdOrderByCreatedAtDesc(UUID senderId, UUID recipientId);
}
