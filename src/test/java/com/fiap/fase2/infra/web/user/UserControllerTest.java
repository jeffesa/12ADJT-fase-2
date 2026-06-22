package com.fiap.fase2.infra.web.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fase2.application.user.*;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.infra.config.SecurityConfig;
import com.fiap.fase2.infra.web.user.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CreateUserUseCase createUseCase;
    @MockBean private UpdateUserUseCase updateUseCase;
    @MockBean private DeleteUserUseCase deleteUseCase;
    @MockBean private FindUserByIdUseCase findByIdUseCase;
    @MockBean private FindAllUsersUseCase findAllUseCase;
    @MockBean private ChangePasswordUseCase changePasswordUseCase;
    @MockBean private LoginUseCase loginUseCase;

    private User buildUser() {
        return new User(UUID.randomUUID(), "João", "joao@email.com", "joao",
                "hash", "Rua A", LocalDateTime.now(), new UserType(UUID.randomUUID(), "CUSTOMER"));
    }

    @Test
    @DisplayName("POST /api/v1/users - deve criar e retornar 201")
    void shouldCreate() throws Exception {
        UUID typeId = UUID.randomUUID();
        when(createUseCase.execute(any(), any(), any(), any(), any(), any())).thenReturn(buildUser());

        UserRequest request = new UserRequest("João", "joao@email.com", "joao", "Senha123", "Rua A", typeId);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("João"));
    }

    @Test
    @DisplayName("POST /api/v1/users - deve retornar 422 com email duplicado")
    void shouldReturn422DuplicateEmail() throws Exception {
        UUID typeId = UUID.randomUUID();
        when(createUseCase.execute(any(), any(), any(), any(), any(), any()))
                .thenThrow(new BusinessException("Email já cadastrado"));

        UserRequest request = new UserRequest("João", "joao@email.com", "joao", "Senha123", "Rua A", typeId);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("GET /api/v1/users - deve listar")
    void shouldFindAll() throws Exception {
        when(findAllUseCase.execute(isNull())).thenReturn(List.of(buildUser()));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("João"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - deve retornar por ID")
    void shouldFindById() throws Exception {
        User user = buildUser();
        when(findByIdUseCase.execute(user.getId())).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - deve retornar 404")
    void shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(findByIdUseCase.execute(id)).thenThrow(new EntityNotFoundException("Não encontrado"));

        mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - deve retornar 204")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id}/password - deve trocar senha")
    void shouldChangePassword() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("Senha123", "NovaSenha456");

        mockMvc.perform(patch("/api/v1/users/" + UUID.randomUUID() + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Senha alterada com sucesso"));
    }

    @Test
    @DisplayName("POST /api/v1/users/login - deve realizar login")
    void shouldLogin() throws Exception {
        when(loginUseCase.execute("joao", "Senha123")).thenReturn(buildUser());

        LoginRequest request = new LoginRequest("joao", "Senha123");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso"));
    }
}
