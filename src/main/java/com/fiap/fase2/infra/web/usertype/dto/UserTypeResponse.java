package com.fiap.fase2.infra.web.usertype.dto;

import com.fiap.fase2.domain.usertype.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados de retorno do tipo de usuário")
public record UserTypeResponse(
        @Schema(description = "ID do tipo de usuário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do tipo de usuário", example = "CUSTOMER")
        String name
) {
    public static UserTypeResponse fromDomain(UserType userType) {
        return new UserTypeResponse(userType.getId(), userType.getName());
    }
}
