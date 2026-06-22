package com.fiap.fase2.infra.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para login")
public record LoginRequest(
        @NotBlank(message = "O login é obrigatório")
        @Schema(description = "Login do usuário", example = "joaosilva")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Schema(description = "Senha do usuário", example = "Senha123")
        String password
) {}
