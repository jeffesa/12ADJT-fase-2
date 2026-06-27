package com.fiap.fase2.infra.web.menuitem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fase2.application.menuitem.*;
import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.infra.config.SecurityConfig;
import com.fiap.fase2.infra.web.menuitem.dto.MenuItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuItemController.class)
@Import(SecurityConfig.class)
class MenuItemControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CreateMenuItemUseCase createUseCase;
    @MockBean private UpdateMenuItemUseCase updateUseCase;
    @MockBean private DeleteMenuItemUseCase deleteUseCase;
    @MockBean private FindMenuItemByIdUseCase findByIdUseCase;
    @MockBean private FindMenuItemsByRestaurantUseCase findByRestaurantUseCase;

    private MenuItem buildItem() {
        return new MenuItem(UUID.randomUUID(), "Pizza", "Margherita",
                new BigDecimal("39.90"), false, "/img/pizza.jpg", UUID.randomUUID());
    }

    @Test
    @DisplayName("POST - deve criar e retornar 201")
    void shouldCreate() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        when(createUseCase.execute(any(), any(), any(), anyBoolean(), any(), eq(restaurantId)))
                .thenReturn(buildItem());

        MenuItemRequest request = new MenuItemRequest("Pizza", "Margherita", new BigDecimal("39.90"), false, "/img/pizza.jpg");

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    @DisplayName("GET por restaurante - deve listar")
    void shouldFindByRestaurant() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        when(findByRestaurantUseCase.execute(restaurantId)).thenReturn(List.of(buildItem()));

        mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    @Test
    @DisplayName("GET por ID - deve retornar")
    void shouldFindById() throws Exception {
        MenuItem item = buildItem();
        when(findByIdUseCase.execute(item.getId())).thenReturn(item);

        mockMvc.perform(get("/api/v1/menu-items/" + item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    @DisplayName("GET por ID - deve retornar 404")
    void shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(findByIdUseCase.execute(id)).thenThrow(new EntityNotFoundException("Não encontrado"));

        mockMvc.perform(get("/api/v1/menu-items/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT - deve atualizar")
    void shouldUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        when(updateUseCase.execute(eq(id), any(), any(), any(), anyBoolean(), any()))
                .thenReturn(buildItem());

        MenuItemRequest request = new MenuItemRequest("Sushi", "Salmão", new BigDecimal("45.00"), true, null);

        mockMvc.perform(put("/api/v1/menu-items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE - deve retornar 204")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/menu-items/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST - deve retornar 400 com dados inválidos")
    void shouldReturn400() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        MenuItemRequest request = new MenuItemRequest("", null, null, false, null);

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST - deve retornar 400 com preço zero")
    void shouldReturn400WhenPriceZero() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        MenuItemRequest request = new MenuItemRequest("Pizza", "desc", BigDecimal.ZERO, false, null);

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.price").exists());
    }

    @Test
    @DisplayName("POST - deve retornar 400 com preço negativo")
    void shouldReturn400WhenPriceNegative() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        MenuItemRequest request = new MenuItemRequest("Pizza", "desc", new BigDecimal("-10"), false, null);

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.price").exists());
    }

    @Test
    @DisplayName("POST - deve retornar 404 com restaurante inexistente")
    void shouldReturn404WhenRestaurantNotFound() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        when(createUseCase.execute(any(), any(), any(), anyBoolean(), any(), eq(restaurantId)))
                .thenThrow(new EntityNotFoundException("Restaurante não encontrado"));

        MenuItemRequest request = new MenuItemRequest("Pizza", "desc", new BigDecimal("39.90"), false, null);

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE - deve retornar 404 com ID inexistente")
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new EntityNotFoundException("Não encontrado")).when(deleteUseCase).execute(id);

        mockMvc.perform(delete("/api/v1/menu-items/" + id))
                .andExpect(status().isNotFound());
    }
}
