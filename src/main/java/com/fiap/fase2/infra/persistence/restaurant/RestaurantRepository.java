package com.fiap.fase2.infra.persistence.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    @Query("SELECT r FROM RestaurantJpaEntity r WHERE r.owner.id = :ownerId")
    List<RestaurantJpaEntity> findByOwnerId(@Param("ownerId") UUID ownerId);
}
