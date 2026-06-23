package com.fiap.fase2.domain.restaurant;

import java.time.LocalDateTime;
import java.util.UUID;

public class Restaurant {
    private UUID id;
    private String name;
    private String address;
    private String cuisineType;
    private LocalDateTime openingHours;
    private LocalDateTime closingTime;
    private UUID ownerId;

    public Restaurant() {
    }

    public Restaurant(UUID id, String name, String address, String cuisineType, LocalDateTime openingHours, LocalDateTime closingTime, UUID ownerId) {
        validateName(name);
        validateAddress(address);
        validateCuisineType(cuisineType);
        validateOpeningHours(openingHours);
        validateClosingTime(closingTime);
        validateOwnerId(ownerId);

        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.closingTime = closingTime;
        this.ownerId = ownerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        validateCuisineType(cuisineType);
        this.cuisineType = cuisineType;
    }

    public LocalDateTime getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(LocalDateTime openingHours) {
        validateOpeningHours(openingHours);
        this.openingHours = openingHours;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        validateClosingTime(closingTime);
        this.closingTime = closingTime;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        validateOwnerId(ownerId);
        this.ownerId = ownerId;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do restaurante é obrigatório");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Endereço do restaurante é obrigatório");
        }
    }

    private void validateCuisineType(String cuisineType) {
        if (cuisineType == null || cuisineType.isBlank()) {
            throw new IllegalArgumentException("Tipo de cozinha é obrigatório");
        }
    }

    private void validateOpeningHours(LocalDateTime openingHours) {
        if (openingHours == null) {
            throw new IllegalArgumentException("Horário de funcionamento é obrigatório");
        }
    }

    private void validateClosingTime(LocalDateTime closingTime) {
        if (closingTime == null) {
            throw new IllegalArgumentException("Horário de fechamento é obrigatório");
        }
    }

    private void validateOwnerId(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("ID do proprietário é obrigatório");
        }
    }
}
