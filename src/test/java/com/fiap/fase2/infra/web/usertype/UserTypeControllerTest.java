package com.fiap.fase2.infra.web.usertype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fase2.application.usertype.*;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.infra.config.SecurityConfig;
import com.fiap.fase2.infra.web.usertype.dto.UserTypeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserTypeController.class)
@Import(SecurityConfig.class)
class UserTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateUserTypeUseCase createUseCase;
    @MockBean
    private UpdateUserTypeUseCase updateUseCase;
    @MockBean
    private DeleteUserTypeUseCase deleteUseCase;
    @MockBean
    private FindUserTypeByIdUseCase findByIdUseCase;
    @MockBean
    private FindAllUserTypesUseCase findAllUseCase;

    @Test
    @DisplayName("POST /api/v1/user-types - deve criar e retornar 201")
    void shouldCreate() throws Exception {
        UUID id = UUID.randomUUID();
        when(createUseCase.execute("CUSTOMER")).thenReturn(new UserType(id, "CUSTOMER"));

        mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserTypeRequest("CUSTOMER"))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("CUSTOMER"));
    }

    @Test
    @DisplayName("POST /api/v1/user-types - deve retornar 400 com nome vazio")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserTypeRequest(""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.name").exists());
    }

    @Test
    @DisplayName("GET /api/v1/user-types - deve listar todos")
    void shouldFindAll() throws Exception {
        when(findAllUseCase.execute()).thenReturn(List.of(
                new UserType(UUID.randomUUID(), "CUSTOMER"),
                new UserType(UUID.randomUUID(), "RESTAURANT_OWNER")
        ));

        mockMvc.perform(get("/api/v1/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("CUSTOMER"))
                .andExpect(jsonPath("$[1].name").value("RESTAURANT_OWNER"));
    }

    @Test
    @DisplayName("GET /api/v1/user-types/{id} - deve retornar por ID")
    void shouldFindById() throws Exception {
        UUID id = UUID.randomUUID();
        when(findByIdUseCase.execute(id)).thenReturn(new UserType(id, "CUSTOMER"));

        mockMvc.perform(get("/api/v1/user-types/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CUSTOMER"));
    }

    @Test
    @DisplayName("GET /api/v1/user-types/{id} - deve retornar 404")
    void shouldReturn404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(findByIdUseCase.execute(id)).thenThrow(new EntityNotFoundException("Não encontrado"));

        mockMvc.perform(get("/api/v1/user-types/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/user-types/{id} - deve atualizar")
    void shouldUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        when(updateUseCase.execute(eq(id), eq("ADMIN"))).thenReturn(new UserType(id, "ADMIN"));

        mockMvc.perform(put("/api/v1/user-types/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserTypeRequest("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @DisplayName("DELETE /api/v1/user-types/{id} - deve retornar 204")
    void shouldDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/user-types/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/user-types/{id} - deve retornar 404")
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new EntityNotFoundException("Não encontrado")).when(deleteUseCase).execute(id);

        mockMvc.perform(delete("/api/v1/user-types/" + id))
                .andExpect(status().isNotFound());
    }
}
