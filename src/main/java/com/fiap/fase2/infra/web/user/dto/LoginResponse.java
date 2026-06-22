package com.fiap.fase2.infra.web.user.dto;

import com.fiap.fase2.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de retorno do login")
public record LoginResponse(
        String message,
        UUID userId,
        String login,
        String email,
        LocalDateTime lastModifiedDate
) {
    public static LoginResponse fromDomain(User user) {
        return new LoginResponse(
                "Login realizado com sucesso",
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getLastModifiedDate()
        );
    }
}
