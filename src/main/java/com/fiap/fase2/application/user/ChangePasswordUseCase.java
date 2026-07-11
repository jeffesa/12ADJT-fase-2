package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.PasswordHasher;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChangePasswordUseCase {

    private final UserGateway userGateway;
    private final PasswordHasher passwordHasher;

    public ChangePasswordUseCase(UserGateway userGateway, PasswordHasher passwordHasher) {
        this.userGateway = userGateway;
        this.passwordHasher = passwordHasher;
    }

    public void execute(UUID id, String currentPassword, String newPassword) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));

        if (!passwordHasher.matches(currentPassword, user.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        validatePasswordStrength(newPassword);

        user.setPassword(passwordHasher.encode(newPassword));
        user.setLastModifiedDate(LocalDateTime.now());
        userGateway.update(user);
    }

    private void validatePasswordStrength(String password) {
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
