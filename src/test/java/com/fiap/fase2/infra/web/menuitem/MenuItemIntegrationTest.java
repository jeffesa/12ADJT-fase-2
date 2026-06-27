package com.fiap.fase2.infra.web.menuitem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fase2.infra.persistence.restaurant.RestaurantJpaEntity;
import com.fiap.fase2.infra.persistence.restaurant.RestaurantRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class MenuItemIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RestaurantRepository restaurantRepository;

    private static String restaurantId;
    private static String menuItemId;

    @BeforeEach
    void setUp() {
        if (restaurantId == null) {
            RestaurantJpaEntity restaurant = restaurantRepository.save(
                    new RestaurantJpaEntity(UUID.randomUUID(), "Pizzaria", "Rua A, 123",
                            "ITALIANA", LocalDateTime.of(2024, 1, 1, 11, 0),
                            LocalDateTime.of(2024, 1, 1, 23, 0), UUID.randomUUID()));
            restaurantId = restaurant.getId().toString();
        }
    }

    @Test
    @Order(1)
    @DisplayName("GET - Deve retornar lista vazia inicialmente")
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(2)
    @DisplayName("POST - Deve criar item e retornar 201")
    void shouldCreate() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Pizza Margherita",
                "description", "Molho, mussarela e manjericão",
                "price", 39.90,
                "dineInOnly", false,
                "photoPath", "/img/pizza.jpg"
        );

        MvcResult result = mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Pizza Margherita"))
                .andExpect(jsonPath("$.price").value(39.90))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId))
                .andReturn();

        menuItemId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(3)
    @DisplayName("GET - Deve listar itens do restaurante")
    void shouldListByRestaurant() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Pizza Margherita"));
    }

    @Test
    @Order(4)
    @DisplayName("GET /{id} - Deve buscar por ID")
    void shouldFindById() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/" + menuItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Margherita"))
                .andExpect(jsonPath("$.description").value("Molho, mussarela e manjericão"));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /{id} - Deve atualizar item")
    void shouldUpdate() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Pizza Calabresa",
                "description", "Calabresa, cebola e azeitona",
                "price", 42.90,
                "dineInOnly", true,
                "photoPath", "/img/calabresa.jpg"
        );

        mockMvc.perform(put("/api/v1/menu-items/" + menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Calabresa"))
                .andExpect(jsonPath("$.price").value(42.90))
                .andExpect(jsonPath("$.dineInOnly").value(true));
    }

    @Test
    @Order(6)
    @DisplayName("GET /{id} - Deve confirmar atualização")
    void shouldConfirmUpdate() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/" + menuItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Calabresa"));
    }

    @Test
    @Order(7)
    @DisplayName("DELETE /{id} - Deve remover e retornar 204")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/menu-items/" + menuItemId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("GET /{id} - Deve retornar 404 após remoção")
    void shouldReturn404AfterDelete() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/" + menuItemId))
                .andExpect(status().isNotFound());
    }

    // --- Cenários de erro ---

    @Test
    @Order(10)
    @DisplayName("POST - Deve retornar 400 com nome vazio")
    void shouldReturn400WhenNameEmpty() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "",
                "price", 10.0,
                "dineInOnly", false
        );

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    @DisplayName("POST - Deve retornar 400 com preço zero")
    void shouldReturn400WhenPriceZero() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Item",
                "price", 0,
                "dineInOnly", false
        );

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    @DisplayName("POST - Deve retornar 404 com restaurante inexistente")
    void shouldReturn404WhenRestaurantNotFound() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Item",
                "price", 10.0,
                "dineInOnly", false
        );

        mockMvc.perform(post("/api/v1/restaurants/00000000-0000-0000-0000-000000000000/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    @DisplayName("GET /{id} - Deve retornar 404 para ID inexistente")
    void shouldReturn404ForNonExistentId() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(14)
    @DisplayName("PUT /{id} - Deve retornar 404 para ID inexistente")
    void shouldReturn404WhenUpdateNonExistent() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Item", "price", 10.0, "dineInOnly", false
        );

        mockMvc.perform(put("/api/v1/menu-items/00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(15)
    @DisplayName("DELETE /{id} - Deve retornar 404 para ID inexistente")
    void shouldReturn404WhenDeleteNonExistent() throws Exception {
        mockMvc.perform(delete("/api/v1/menu-items/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }
}
