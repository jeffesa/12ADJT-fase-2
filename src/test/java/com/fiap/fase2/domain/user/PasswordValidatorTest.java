package com.fiap.fase2.domain.user;

import com.fiap.fase2.domain.shared.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    @DisplayName("Deve aceitar senha válida")
    void shouldAcceptValidPassword() {
        assertDoesNotThrow(() -> PasswordValidator.validate("Senha123"));
    }

    @Test
    @DisplayName("Deve rejeitar senha nula")
    void shouldRejectNullPassword() {
        BusinessException ex = assertThrows(BusinessException.class, () -> PasswordValidator.validate(null));
        assertTrue(ex.getMessage().contains("mínimo 8"));
    }

    @Test
    @DisplayName("Deve rejeitar senha curta")
    void shouldRejectShortPassword() {
        BusinessException ex = assertThrows(BusinessException.class, () -> PasswordValidator.validate("Ab1"));
        assertTrue(ex.getMessage().contains("mínimo 8"));
    }

    @Test
    @DisplayName("Deve rejeitar senha sem maiúscula")
    void shouldRejectPasswordWithoutUppercase() {
        BusinessException ex = assertThrows(BusinessException.class, () -> PasswordValidator.validate("senha123"));
        assertTrue(ex.getMessage().contains("maiúscula"));
    }

    @Test
    @DisplayName("Deve rejeitar senha sem minúscula")
    void shouldRejectPasswordWithoutLowercase() {
        BusinessException ex = assertThrows(BusinessException.class, () -> PasswordValidator.validate("SENHA123"));
        assertTrue(ex.getMessage().contains("minúscula"));
    }

    @Test
    @DisplayName("Deve rejeitar senha sem número")
    void shouldRejectPasswordWithoutNumber() {
        BusinessException ex = assertThrows(BusinessException.class, () -> PasswordValidator.validate("SenhaForte"));
        assertTrue(ex.getMessage().contains("número"));
    }
}
