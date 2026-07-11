package com.fiap.fase2.domain.user;

import com.fiap.fase2.domain.shared.BusinessException;

public final class PasswordValidator {

    private PasswordValidator() {}

    public static void validate(String password) {
        if (password == null || password.length() < 8) {
            throw new BusinessException("A senha deve ter no mínimo 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new BusinessException("A senha deve conter pelo menos uma letra maiúscula");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new BusinessException("A senha deve conter pelo menos uma letra minúscula");
        }
        if (!password.matches(".*\\d.*")) {
            throw new BusinessException("A senha deve conter pelo menos um número");
        }
    }
}
