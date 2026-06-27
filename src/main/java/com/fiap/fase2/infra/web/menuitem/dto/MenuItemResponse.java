package com.fiap.fase2.infra.web.menuitem.dto;

import com.fiap.fase2.domain.menuitem.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados de retorno do item do cardápio")
public record MenuItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        boolean dineInOnly,
        String photoPath,
        UUID restaurantId
) {
    public static MenuItemResponse fromDomain(MenuItem item) {
        return new MenuItemResponse(
                item.getId(), item.getName(), item.getDescription(),
                item.getPrice(), item.isDineInOnly(), item.getPhotoPath(),
                item.getRestaurantId()
        );
    }
}
