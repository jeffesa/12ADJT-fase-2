package com.fiap.fase2.infra.web.user;

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
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USERS_URL = "/api/v1/users";
    private static final String USER_TYPES_URL = "/api/v1/user-types";
    private static String userTypeId;
    private static String userId;

    @Test
    @Order(1)
    @DisplayName("Setup - Criar tipo de usuário CUSTOMER")
    void shouldCreateUserType() throws Exception {
        MvcResult result = mockMvc.perform(post(USER_TYPES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "CUSTOMER"))))
                .andExpect(status().isCreated())
                .andReturn();

        userTypeId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(2)
    @DisplayName("POST - Deve criar usuário e retornar 201")
    void shouldCreateUser() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "João Silva",
                "email", "joao@email.com",
                "login", "joaosilva",
                "password", "Senha123",
                "address", "Rua das Flores, 123",
                "userTypeId", userTypeId
        );

        MvcResult result = mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.userTypeName").value("CUSTOMER"))
                .andReturn();

        userId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    @Order(3)
    @DisplayName("GET - Deve listar usuários")
    void shouldListUsers() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    @Order(4)
    @DisplayName("GET /{id} - Deve buscar por ID")
    void shouldFindById() throws Exception {
        mockMvc.perform(get(USERS_URL + "/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    @Order(5)
    @DisplayName("GET ?name= - Deve buscar por nome")
    void shouldFindByName() throws Exception {
        mockMvc.perform(get(USERS_URL).param("name", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(6)
    @DisplayName("PUT - Deve atualizar usuário")
    void shouldUpdate() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "João Atualizado",
                "email", "joao@email.com",
                "login", "joaosilva",
                "address", "Rua Nova, 456",
                "userTypeId", userTypeId
        );

        mockMvc.perform(put(USERS_URL + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Atualizado"))
                .andExpect(jsonPath("$.address").value("Rua Nova, 456"));
    }

    @Test
    @Order(7)
    @DisplayName("POST /login - Deve realizar login")
    void shouldLogin() throws Exception {
        Map<String, String> request = Map.of("login", "joaosilva", "password", "Senha123");

        mockMvc.perform(post(USERS_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso"))
                .andExpect(jsonPath("$.login").value("joaosilva"));
    }

    @Test
    @Order(8)
    @DisplayName("PATCH /{id}/password - Deve trocar senha")
    void shouldChangePassword() throws Exception {
        Map<String, String> request = Map.of("currentPassword", "Senha123", "newPassword", "NovaSenha456");

        mockMvc.perform(patch(USERS_URL + "/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Senha alterada com sucesso"));
    }

    @Test
    @Order(9)
    @DisplayName("POST /login - Deve logar com nova senha")
    void shouldLoginWithNewPassword() throws Exception {
        Map<String, String> request = Map.of("login", "joaosilva", "password", "NovaSenha456");

        mockMvc.perform(post(USERS_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @DisplayName("DELETE - Deve remover usuário")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete(USERS_URL + "/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(11)
    @DisplayName("GET /{id} - Deve retornar 404 após remoção")
    void shouldReturn404AfterDelete() throws Exception {
        mockMvc.perform(get(USERS_URL + "/" + userId))
                .andExpect(status().isNotFound());
    }

    // --- Cenários de erro ---

    @Test
    @Order(20)
    @DisplayName("POST - Deve retornar 422 com email duplicado")
    void shouldReturn422DuplicateEmail() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Maria", "email", "maria@email.com", "login", "maria",
                "password", "Senha123", "address", "Rua B", "userTypeId", userTypeId
        );
        mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Tenta criar com mesmo email
        Map<String, Object> duplicate = Map.of(
                "name", "Outra", "email", "maria@email.com", "login", "outra",
                "password", "Senha123", "address", "Rua C", "userTypeId", userTypeId
        );
        mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(21)
    @DisplayName("POST /login - Deve retornar 422 com credenciais inválidas")
    void shouldReturn422InvalidCredentials() throws Exception {
        Map<String, String> request = Map.of("login", "maria", "password", "errada");

        mockMvc.perform(post(USERS_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(22)
    @DisplayName("PATCH - Deve retornar 422 com senha atual incorreta")
    void shouldReturn422WrongCurrentPassword() throws Exception {
        // Buscar o ID da Maria criada no teste 20
        MvcResult result = mockMvc.perform(get(USERS_URL).param("name", "Maria"))
                .andReturn();
        String mariaId = objectMapper.readTree(result.getResponse().getContentAsString()).get(0).get("id").asText();

        Map<String, String> request = Map.of("currentPassword", "errada", "newPassword", "Nova123456");

        mockMvc.perform(patch(USERS_URL + "/" + mariaId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}
