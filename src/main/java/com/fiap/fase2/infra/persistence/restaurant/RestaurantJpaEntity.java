package com.fiap.fase2.infra.persistence.restaurant;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class RestaurantJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String cuisineType;

    @Column(nullable = false)
    private LocalDateTime openingHours;

    @Column(nullable = false)
    private LocalDateTime closingTime;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    public RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(UUID id, String name, String address, String cuisineType, LocalDateTime openingHours, LocalDateTime closingTime, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.closingTime = closingTime;
        this.ownerId = ownerId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    public LocalDateTime getOpeningHours() { return openingHours; }
    public void setOpeningHours(LocalDateTime openingHours) { this.openingHours = openingHours; }
    public LocalDateTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalDateTime closingTime) { this.closingTime = closingTime; }
    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
}
