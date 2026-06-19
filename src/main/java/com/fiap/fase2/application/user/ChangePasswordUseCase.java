package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChangePasswordUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(UUID id, String currentPassword, String newPassword) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastModifiedDate(LocalDateTime.now());
        userGateway.update(user);
    }
}
