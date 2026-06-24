package com.fiap.fase2.infra.persistence.menuitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemJpaEntity, UUID> {

    List<MenuItemJpaEntity> findByRestaurantId(UUID restaurantId);
}
