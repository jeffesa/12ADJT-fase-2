package com.fiap.fase2.infra.web.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RestaurantIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String RESTAURANTS_URL = "/api/v1/restaurants";
    private static final String USER_TYPES_URL = "/api/v1/user-types";
    private static final String USERS_URL = "/api/v1/users";
    private static String userTypeId;
    private static String ownerId;
    private static String restaurantId;
    private static String customerTypeId;
    private static String customerId;

    @Test
    @Order(1)
    @DisplayName("Setup - Criar tipo RESTAURANT_OWNER")
    void shouldCreateRestaurantOwnerType() throws Exception {
        MvcResult result = mockMvc.perform(post(USER_TYPES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "RESTAURANT_OWNER"))))
                .andExpect(status().isCreated())
                .andReturn();

        userTypeId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(2)
    @DisplayName("Setup - Criar tipo CUSTOMER")
    void shouldCreateCustomerType() throws Exception {
        MvcResult result = mockMvc.perform(post(USER_TYPES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "CUSTOMER"))))
                .andExpect(status().isCreated())
                .andReturn();

        customerTypeId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(3)
    @DisplayName("Setup - Criar usuário RESTAURANT_OWNER")
    void shouldCreateOwnerUser() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Owner Teste",
                "email", "owner@email.com",
                "login", "ownerteste",
                "password", "Senha123",
                "address", "Rua Owner, 1",
                "userTypeId", userTypeId
        );

        MvcResult result = mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        ownerId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(4)
    @DisplayName("Setup - Criar usuário CUSTOMER (para testar erro)")
    void shouldCreateCustomerUser() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Customer Teste",
                "email", "customer@email.com",
                "login", "customerteste",
                "password", "Senha123",
                "address", "Rua Customer, 1",
                "userTypeId", customerTypeId
        );

        MvcResult result = mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        customerId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(5)
    @DisplayName("POST - Deve criar restaurante com owner RESTAURANT_OWNER e retornar 201")
    void shouldCreateRestaurant() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Pizzaria Teste",
                "address", "Rua da Pizza, 100",
                "cuisineType", "ITALIANA",
                "openingHours", "2024-01-01T11:00:00",
                "closingTime", "2024-01-01T23:00:00",
                "ownerId", ownerId
        );

        MvcResult result = mockMvc.perform(post(RESTAURANTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Pizzaria Teste")))
                .andExpect(jsonPath("$.address", is("Rua da Pizza, 100")))
                .andExpect(jsonPath("$.cuisineType", is("ITALIANA")))
                .andExpect(jsonPath("$.ownerId", is(ownerId)))
                .andReturn();

        restaurantId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(6)
    @DisplayName("POST - Deve retornar 422 quando owner é CUSTOMER (não RESTAURANT_OWNER)")
    void shouldReturn422WhenOwnerIsCustomer() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Restaurante Inválido",
                "address", "Rua X, 1",
                "cuisineType", "BRASILEIRA",
                "openingHours", "2024-01-01T08:00:00",
                "closingTime", "2024-01-01T22:00:00",
                "ownerId", customerId
        );

        mockMvc.perform(post(RESTAURANTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString("RESTAURANT_OWNER")));
    }

    @Test
    @Order(7)
    @DisplayName("POST - Deve retornar 422 quando owner não existe")
    void shouldReturn422WhenOwnerDoesNotExist() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Restaurante Sem Dono",
                "address", "Rua Y, 2",
                "cuisineType", "JAPONESA",
                "openingHours", "2024-01-01T10:00:00",
                "closingTime", "2024-01-01T22:00:00",
                "ownerId", "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
        );

        mockMvc.perform(post(RESTAURANTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString("proprietário")));
    }

    @Test
    @Order(8)
    @DisplayName("POST - Deve retornar 400 quando nome é vazio")
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "",
                "address", "Rua Z, 3",
                "cuisineType", "MEXICANA",
                "openingHours", "2024-01-01T09:00:00",
                "closingTime", "2024-01-01T21:00:00",
                "ownerId", ownerId
        );

        mockMvc.perform(post(RESTAURANTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    @DisplayName("GET - Deve listar todos os restaurantes e retornar 200")
    void shouldListAllRestaurants() throws Exception {
        mockMvc.perform(get(RESTAURANTS_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", notNullValue()));
    }

    @Test
    @Order(10)
    @DisplayName("GET /{id} - Deve buscar restaurante por ID e retornar 200")
    void shouldFindRestaurantById() throws Exception {
        mockMvc.perform(get(RESTAURANTS_URL + "/" + restaurantId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(restaurantId)))
                .andExpect(jsonPath("$.name", is("Pizzaria Teste")))
                .andExpect(jsonPath("$.ownerId", is(ownerId)));
    }

    @Test
    @Order(11)
    @DisplayName("GET /{id} - Deve retornar 404 quando ID não existe")
    void shouldReturn404WhenRestaurantNotFound() throws Exception {
        mockMvc.perform(get(RESTAURANTS_URL + "/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(12)
    @DisplayName("GET /owner/{ownerId} - Deve buscar restaurantes por owner e retornar 200")
    void shouldFindRestaurantsByOwner() throws Exception {
        mockMvc.perform(get(RESTAURANTS_URL + "/owner/" + ownerId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].ownerId", is(ownerId)));
    }

    @Test
    @Order(13)
    @DisplayName("PUT /{id} - Deve atualizar restaurante e retornar 200")
    void shouldUpdateRestaurant() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Pizzaria Atualizada",
                "address", "Rua Nova, 200",
                "cuisineType", "ITALIANA",
                "openingHours", "2024-01-01T10:00:00",
                "closingTime", "2024-01-02T00:00:00",
                "ownerId", ownerId
        );

        mockMvc.perform(put(RESTAURANTS_URL + "/" + restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Pizzaria Atualizada")))
                .andExpect(jsonPath("$.address", is("Rua Nova, 200")));
    }

    @Test
    @Order(14)
    @DisplayName("PUT /{id} - Deve retornar 404 quando restaurante não existe")
    void shouldReturn404WhenUpdatingNonExistentRestaurant() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Teste",
                "address", "Rua Teste",
                "cuisineType", "BRASILEIRA",
                "openingHours", "2024-01-01T08:00:00",
                "closingTime", "2024-01-01T22:00:00",
                "ownerId", ownerId
        );

        mockMvc.perform(put(RESTAURANTS_URL + "/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(15)
    @DisplayName("DELETE /{id} - Deve deletar restaurante e retornar 204")
    void shouldDeleteRestaurant() throws Exception {
        mockMvc.perform(delete(RESTAURANTS_URL + "/" + restaurantId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(16)
    @DisplayName("DELETE /{id} - Deve retornar 404 quando restaurante não existe")
    void shouldReturn404WhenDeletingNonExistentRestaurant() throws Exception {
        mockMvc.perform(delete(RESTAURANTS_URL + "/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(17)
    @DisplayName("GET /{id} - Deve retornar 404 após deletar")
    void shouldReturn404AfterDelete() throws Exception {
        mockMvc.perform(get(RESTAURANTS_URL + "/" + restaurantId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
