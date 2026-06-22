package com.fiap.fase2.infra.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para troca de senha")
public record ChangePasswordRequest(
        @NotBlank(message = "A senha atual é obrigatória")
        @Schema(description = "Senha atual", example = "SenhaAtual123")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres")
        @Schema(description = "Nova senha", example = "NovaSenha456")
        String newPassword
) {}
