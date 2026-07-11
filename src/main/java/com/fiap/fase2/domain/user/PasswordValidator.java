package com.fiap.fase2.domain.user;

import com.fiap.fase2.domain.shared.BusinessException;

public final class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    private PasswordValidator() {}

    public static void validate(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            throw new BusinessException("A senha deve ter no mínimo 8 caracteres");
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasUpper) {
            throw new BusinessException("A senha deve conter pelo menos uma letra maiúscula");
        }
        if (!hasLower) {
            throw new BusinessException("A senha deve conter pelo menos uma letra minúscula");
        }
        if (!hasDigit) {
            throw new BusinessException("A senha deve conter pelo menos um número");
        }
    }
}
