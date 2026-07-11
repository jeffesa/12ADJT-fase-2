package com.fiap.fase2.infra.web.restaurant;

import com.fiap.fase2.application.restaurant.*;
import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantRequest;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateRestaurantUseCase createRestaurantUseCase;
    @MockBean
    private DeleteRestaurantUseCase deleteRestaurantUseCase;
    @MockBean
    private FindAllRestaurantsUseCase findAllRestaurantsUseCase;
    @MockBean
    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    @MockBean
    private FindRestaurantsByOwnerUseCase findRestaurantsByOwnerUseCase;
    @MockBean
    private UpdateRestaurantUseCase updateRestaurantUseCase;

    @Test
    void createRestaurant_shouldReturn201AndLocation() throws Exception {
        UUID ownerId = UUID.randomUUID();
        RestaurantRequest request = new RestaurantRequest("Nome", "Addr", "ITALIANA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), ownerId);

        Restaurant created = new Restaurant(UUID.randomUUID(), request.name(), request.address(),
                request.cuisineType(), request.openingHours(), request.closingTime(), ownerId);

        when(createRestaurantUseCase.execute(anyString(), anyString(), anyString(), any(), any(), any()))
                .thenReturn(created);

        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(created.getId().toString()))
                .andExpect(jsonPath("$.name").value("Nome"));
    }

    @Test
    void createRestaurant_shouldReturn400WhenValidationFails() throws Exception {
        UUID ownerId = UUID.randomUUID();
        RestaurantRequest request = new RestaurantRequest("A", "Addr", "ITALIANA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), ownerId);

        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.name").value("O nome deve ter entre 2 e 100 caracteres"));
    }

    @Test
    void findAllRestaurants_shouldReturnList() throws Exception {
        Restaurant r1 = new Restaurant(UUID.randomUUID(), "A", "Addr A", "ITALIANA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), UUID.randomUUID());
        Restaurant r2 = new Restaurant(UUID.randomUUID(), "B", "Addr B", "BRASILEIRA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 12, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 22, 0), UUID.randomUUID());

        when(findAllRestaurantsUseCase.execute()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v1/restaurants").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findAllRestaurants_shouldFilterByOwnerId() throws Exception {
        UUID ownerId = UUID.randomUUID();
        Restaurant r1 = new Restaurant(UUID.randomUUID(), "A", "Addr A", "ITALIANA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), ownerId);

        when(findRestaurantsByOwnerUseCase.execute(ownerId)).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/v1/restaurants").param("ownerId", ownerId.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ownerId").value(ownerId.toString()));
    }

    @Test
    void findRestaurantById_shouldReturn200WhenFound() throws Exception {
        UUID id = UUID.randomUUID();
        Restaurant r = new Restaurant(id, "A", "Addr A", "ITALIANA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), UUID.randomUUID());

        when(findRestaurantByIdUseCase.execute(id)).thenReturn(r);

        mockMvc.perform(get("/api/v1/restaurants/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void findRestaurantById_shouldReturn404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(findRestaurantByIdUseCase.execute(id))
                .thenThrow(new EntityNotFoundException("Restaurante não encontrado com id: " + id));

        mockMvc.perform(get("/api/v1/restaurants/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRestaurant_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/restaurants/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateRestaurant_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        RestaurantUpdateRequest request = new RestaurantUpdateRequest("Novo", null, null,
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), null);

        Restaurant updated = new Restaurant(id, "Novo", "Addr", "ITALIANA",
                request.openingHours(), request.closingTime(), UUID.randomUUID());

        when(updateRestaurantUseCase.executeWithOwnerCheck(any(UUID.class), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/restaurants/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Novo"));
    }
}
