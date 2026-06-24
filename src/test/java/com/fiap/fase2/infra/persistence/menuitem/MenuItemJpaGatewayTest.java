package com.fiap.fase2.infra.persistence.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(MenuItemJpaGateway.class)
class MenuItemJpaGatewayTest {

    @Autowired
    private MenuItemJpaGateway gateway;

    private MenuItem buildItem(UUID restaurantId) {
        return new MenuItem(UUID.randomUUID(), "Pizza", "Margherita",
                new BigDecimal("39.90"), false, "/img/pizza.jpg", restaurantId);
    }

    @Test
    @DisplayName("Deve criar item")
    void shouldCreate() {
        MenuItem item = buildItem(UUID.randomUUID());
        MenuItem saved = gateway.create(item);
        assertNotNull(saved);
        assertEquals("Pizza", saved.getName());
    }

    @Test
    @DisplayName("Deve buscar por ID")
    void shouldFindById() {
        MenuItem item = gateway.create(buildItem(UUID.randomUUID()));
        Optional<MenuItem> result = gateway.findById(item.getId());
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Deve retornar vazio para ID inexistente")
    void shouldReturnEmptyForNonExistentId() {
        assertTrue(gateway.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    @DisplayName("Deve buscar por restaurantId")
    void shouldFindByRestaurantId() {
        UUID restaurantId = UUID.randomUUID();
        gateway.create(buildItem(restaurantId));
        gateway.create(buildItem(restaurantId));
        gateway.create(buildItem(UUID.randomUUID()));

        List<MenuItem> result = gateway.findByRestaurantId(restaurantId);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve atualizar item")
    void shouldUpdate() {
        MenuItem item = gateway.create(buildItem(UUID.randomUUID()));
        item.setName("Sushi");
        item.setPrice(new BigDecimal("45.00"));

        MenuItem updated = gateway.update(item);
        assertEquals("Sushi", updated.getName());
        assertEquals(new BigDecimal("45.00"), updated.getPrice());
    }

    @Test
    @DisplayName("Deve deletar item")
    void shouldDelete() {
        MenuItem item = gateway.create(buildItem(UUID.randomUUID()));
        gateway.delete(item.getId());
        assertTrue(gateway.findById(item.getId()).isEmpty());
    }
}
