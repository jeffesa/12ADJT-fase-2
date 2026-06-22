package com.fiap.fase2.domain.restaurant;

import java.util.UUID;

public class Restaurant {
    private UUID id;
    private String name;
    private String address;
    private String cuisineType;
    private String openingHours;
    private UUID ownerId;

    public Restaurant() {
    }

    public Restaurant(UUID id, String name, String address, String cuisineType, String openingHours, UUID ownerId) {
        validateName(name);
        validateAddress(address);
        validateCuisineType(cuisineType);
        validateOpeningHours(openingHours);
        validateOwnerId(ownerId);

        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
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

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        validateOpeningHours(openingHours);
        this.openingHours = openingHours;
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

    private void validateOpeningHours(String openingHours) {
        if (openingHours == null || openingHours.isBlank()) {
            throw new IllegalArgumentException("Horário de funcionamento é obrigatório");
        }
    }

    private void validateOwnerId(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("ID do proprietário é obrigatório");
        }
    }
}
