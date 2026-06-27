package com.fiap.fase2.infra.persistence.menuitem;

import com.fiap.fase2.infra.persistence.restaurant.RestaurantJpaEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantJpaEntity restaurant;

    public MenuItemJpaEntity() {
    }

    public MenuItemJpaEntity(UUID id, String name, String description, BigDecimal price,
                             boolean dineInOnly, String photoPath, RestaurantJpaEntity restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
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
    public RestaurantJpaEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantJpaEntity restaurant) { this.restaurant = restaurant; }
}
