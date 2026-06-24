package com.fiap.fase2.infra.persistence.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MenuItemJpaGateway implements MenuItemGateway {

    private final MenuItemRepository repository;

    public MenuItemJpaGateway(MenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public MenuItem create(MenuItem menuItem) {
        return toDomain(repository.save(toJpaEntity(menuItem)));
    }

    @Override
    public MenuItem update(MenuItem menuItem) {
        return toDomain(repository.save(toJpaEntity(menuItem)));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<MenuItem> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<MenuItem> findByRestaurantId(UUID restaurantId) {
        return repository.findByRestaurantId(restaurantId).stream()
                .map(this::toDomain)
                .toList();
    }

    private MenuItemJpaEntity toJpaEntity(MenuItem menuItem) {
        return new MenuItemJpaEntity(
                menuItem.getId(), menuItem.getName(), menuItem.getDescription(),
                menuItem.getPrice(), menuItem.isDineInOnly(), menuItem.getPhotoPath(),
                menuItem.getRestaurantId()
        );
    }

    private MenuItem toDomain(MenuItemJpaEntity entity) {
        return new MenuItem(
                entity.getId(), entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.isDineInOnly(), entity.getPhotoPath(),
                entity.getRestaurantId()
        );
    }
}
