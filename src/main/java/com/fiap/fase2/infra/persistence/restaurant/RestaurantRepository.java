package com.fiap.fase2.infra.persistence.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    List<RestaurantJpaEntity> findByOwnerId(UUID ownerId);
}
