package com.fiap.fase2.infra.web.usertype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação/atualização de tipo de usuário")
public record UserTypeRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
        @Schema(description = "Nome do tipo de usuário", example = "CUSTOMER")
        String name
) {}
