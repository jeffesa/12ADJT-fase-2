package com.fiap.fase2.infra.web.usertype;

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
class UserTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/user-types";
    private static String createdId;

    @Test
    @Order(1)
    @DisplayName("GET - Deve retornar lista vazia inicialmente")
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(2)
    @DisplayName("POST - Deve criar tipo de usuário e retornar 201")
    void shouldCreate() throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "CUSTOMER"))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("CUSTOMER"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        createdId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    @Order(3)
    @DisplayName("GET - Deve listar o tipo criado")
    void shouldListCreated() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("CUSTOMER"));
    }

    @Test
    @Order(4)
    @DisplayName("GET /{id} - Deve buscar por ID")
    void shouldFindById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.name").value("CUSTOMER"));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /{id} - Deve atualizar")
    void shouldUpdate() throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "RESTAURANT_OWNER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RESTAURANT_OWNER"));
    }

    @Test
    @Order(6)
    @DisplayName("GET /{id} - Deve confirmar atualização")
    void shouldConfirmUpdate() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RESTAURANT_OWNER"));
    }

    @Test
    @Order(7)
    @DisplayName("DELETE /{id} - Deve remover e retornar 204")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + createdId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("GET /{id} - Deve retornar 404 após remoção")
    void shouldReturn404AfterDelete() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + createdId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- Cenários de erro ---

    @Test
    @Order(10)
    @DisplayName("POST - Deve retornar 400 com nome vazio")
    void shouldReturn400WhenNameEmpty() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.name").exists());
    }

    @Test
    @Order(11)
    @DisplayName("POST - Deve retornar 400 com body vazio")
    void shouldReturn400WhenBodyEmpty() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    @DisplayName("GET /{id} - Deve retornar 404 para ID inexistente")
    void shouldReturn404ForNonExistentId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    @DisplayName("PUT /{id} - Deve retornar 404 para ID inexistente")
    void shouldReturn404WhenUpdateNonExistent() throws Exception {
        mockMvc.perform(put(BASE_URL + "/00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "NOVO"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(14)
    @DisplayName("DELETE /{id} - Deve retornar 404 para ID inexistente")
    void shouldReturn404WhenDeleteNonExistent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(20)
    @DisplayName("POST - Deve retornar 422 ao criar com nome duplicado")
    void shouldReturn422WhenNameAlreadyExists() throws Exception {
        // Cria primeiro
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "DUPLICADO"))))
                .andExpect(status().isCreated());

        // Tenta criar com mesmo nome
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "DUPLICADO"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Tipo de usuário já existe com nome: DUPLICADO"));
    }
}
