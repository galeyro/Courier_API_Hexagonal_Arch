package com.courier.api.shipments.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, UUID> {

    List<ShipmentEntity> findBySenderIdOrRecipientIdOrderByCreatedAtDesc(UUID senderId, UUID recipientId);
}
