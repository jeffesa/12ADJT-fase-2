package com.fiap.fase2.infra.web.user.dto;

import com.fiap.fase2.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de retorno do usuário")
public record UserResponse(
        UUID id,
        String name,
        String email,
        String login,
        String address,
        UUID userTypeId,
        String userTypeName,
        LocalDateTime lastModifiedDate
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getAddress(),
                user.getUserType() != null ? user.getUserType().getId() : null,
                user.getUserType() != null ? user.getUserType().getName() : null,
                user.getLastModifiedDate()
        );
    }
}
