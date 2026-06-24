package com.fiap.fase2.infra.persistence.menuitem;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class MenuItemJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean dineInOnly;

    private String photoPath;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    public MenuItemJpaEntity() {
    }

    public MenuItemJpaEntity(UUID id, String name, String description, BigDecimal price,
                             boolean dineInOnly, String photoPath, UUID restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public boolean isDineInOnly() { return dineInOnly; }
    public void setDineInOnly(boolean dineInOnly) { this.dineInOnly = dineInOnly; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
}
