package com.fiap.fase2.domain.menuitem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    @DisplayName("Deve criar MenuItem com dados válidos")
    void shouldCreateWithValidData() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        MenuItem item = new MenuItem(id, "Pizza Margherita", "Molho, mussarela, manjericão",
                new BigDecimal("39.90"), false, "/img/pizza.jpg", restaurantId);

        assertEquals(id, item.getId());
        assertEquals("Pizza Margherita", item.getName());
        assertEquals("Molho, mussarela, manjericão", item.getDescription());
        assertEquals(new BigDecimal("39.90"), item.getPrice());
        assertFalse(item.isDineInOnly());
        assertEquals("/img/pizza.jpg", item.getPhotoPath());
        assertEquals(restaurantId, item.getRestaurantId());
    }

    @Test
    @DisplayName("Deve criar com construtor padrão")
    void shouldCreateWithDefaultConstructor() {
        MenuItem item = new MenuItem();
        assertNotNull(item);
        assertNull(item.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção com nome nulo")
    void shouldThrowWhenNameNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new MenuItem(UUID.randomUUID(), null, "desc", BigDecimal.TEN, false, null, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com nome vazio")
    void shouldThrowWhenNameBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new MenuItem(UUID.randomUUID(), "  ", "desc", BigDecimal.TEN, false, null, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com preço nulo")
    void shouldThrowWhenPriceNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new MenuItem(UUID.randomUUID(), "Pizza", "desc", null, false, null, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com preço zero")
    void shouldThrowWhenPriceZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new MenuItem(UUID.randomUUID(), "Pizza", "desc", BigDecimal.ZERO, false, null, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com preço negativo")
    void shouldThrowWhenPriceNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new MenuItem(UUID.randomUUID(), "Pizza", "desc", new BigDecimal("-1"), false, null, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com restaurantId nulo")
    void shouldThrowWhenRestaurantIdNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new MenuItem(UUID.randomUUID(), "Pizza", "desc", BigDecimal.TEN, false, null, null));
    }

    @Test
    @DisplayName("Deve alterar campos via setters")
    void shouldSetFields() {
        MenuItem item = new MenuItem(UUID.randomUUID(), "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());

        item.setName("Sushi");
        item.setDescription("Salmão fresco");
        item.setPrice(new BigDecimal("45.00"));
        item.setDineInOnly(true);
        item.setPhotoPath("/img/sushi.jpg");

        assertEquals("Sushi", item.getName());
        assertEquals("Salmão fresco", item.getDescription());
        assertEquals(new BigDecimal("45.00"), item.getPrice());
        assertTrue(item.isDineInOnly());
        assertEquals("/img/sushi.jpg", item.getPhotoPath());
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar nome nulo via setter")
    void shouldThrowWhenSetNameNull() {
        MenuItem item = new MenuItem(UUID.randomUUID(), "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> item.setName(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar preço zero via setter")
    void shouldThrowWhenSetPriceZero() {
        MenuItem item = new MenuItem(UUID.randomUUID(), "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> item.setPrice(BigDecimal.ZERO));
    }
}
