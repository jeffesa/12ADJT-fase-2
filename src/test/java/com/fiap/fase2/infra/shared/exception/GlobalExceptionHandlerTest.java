package com.fiap.fase2.infra.shared.exception;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    @DisplayName("Deve retornar 404 para EntityNotFoundException")
    void shouldReturn404ForEntityNotFound() {
        var ex = new EntityNotFoundException("Recurso não encontrado com id: 1");

        ProblemDetail result = handler.handleEntityNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        assertEquals("Recurso não encontrado", result.getTitle());
        assertEquals("Recurso não encontrado com id: 1", result.getDetail());
    }

    @Test
    @DisplayName("Deve retornar 422 para BusinessException")
    void shouldReturn422ForBusinessException() {
        var ex = new BusinessException("Usuário deve ser RESTAURANT_OWNER");

        ProblemDetail result = handler.handleBusiness(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getStatus());
        assertEquals("Erro de negócio", result.getTitle());
        assertEquals("Usuário deve ser RESTAURANT_OWNER", result.getDetail());
    }

    @Test
    @DisplayName("Deve retornar 400 para MethodArgumentNotValidException")
    @SuppressWarnings("unchecked")
    void shouldReturn400ForValidationErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "name", "O nome é obrigatório");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        var ex = new MethodArgumentNotValidException(null, bindingResult);

        ProblemDetail result = handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Dados inválidos", result.getTitle());
        assertNotNull(result.getProperties());
        Map<String, String> campos = (Map<String, String>) result.getProperties().get("campos");
        assertEquals("O nome é obrigatório", campos.get("name"));
    }

    @Test
    @DisplayName("Deve retornar 409 para DataIntegrityViolationException")
    void shouldReturn409ForDataIntegrity() {
        var ex = new DataIntegrityViolationException("unique constraint", new RuntimeException("duplicate key"));

        ProblemDetail result = handler.handleDataIntegrity(ex, request);

        assertEquals(HttpStatus.CONFLICT.value(), result.getStatus());
        assertEquals("Conflito de dados", result.getTitle());
    }

    @Test
    @DisplayName("Deve retornar 400 para IllegalArgumentException")
    void shouldReturn400ForIllegalArgument() {
        var ex = new IllegalArgumentException("Senha atual incorreta");

        ProblemDetail result = handler.handleIllegalArgument(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Requisição inválida", result.getTitle());
        assertEquals("Senha atual incorreta", result.getDetail());
    }
}
