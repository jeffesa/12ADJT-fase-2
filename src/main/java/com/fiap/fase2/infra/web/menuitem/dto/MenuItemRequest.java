package com.fiap.fase2.infra.web.menuitem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados para criação/atualização de item do cardápio")
public record MenuItemRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100)
        @Schema(description = "Nome do item", example = "Pizza Margherita")
        String name,

        @Schema(description = "Descrição do item", example = "Molho, mussarela e manjericão")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        @Schema(description = "Preço", example = "39.90")
        BigDecimal price,

        @Schema(description = "Disponível apenas no local", example = "false")
        boolean dineInOnly,

        @Schema(description = "Caminho da foto", example = "/img/pizza.jpg")
        String photoPath
) {}
