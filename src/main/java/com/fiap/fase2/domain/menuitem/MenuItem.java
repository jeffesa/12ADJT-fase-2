package com.fiap.fase2.domain.menuitem;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItem {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean dineInOnly;
    private String photoPath;
    private UUID restaurantId;

    public MenuItem() {
    }

    public MenuItem(UUID id, String name, String description, BigDecimal price,
                    boolean dineInOnly, String photoPath, UUID restaurantId) {
        validateName(name);
        validatePrice(price);
        validateRestaurantId(restaurantId);
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
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public boolean isDineInOnly() { return dineInOnly; }
    public void setDineInOnly(boolean dineInOnly) { this.dineInOnly = dineInOnly; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) {
        validateRestaurantId(restaurantId);
        this.restaurantId = restaurantId;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do item é obrigatório");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
    }

    private void validateRestaurantId(UUID restaurantId) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("ID do restaurante é obrigatório");
        }
    }
}
