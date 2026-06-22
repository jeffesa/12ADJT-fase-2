package com.fiap.fase2.infra.persistence.user;

import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.infra.persistence.usertype.UserTypeJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserJpaGateway implements UserGateway {

    private final UserRepository repository;

    public UserJpaGateway(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return toDomain(repository.save(toJpaEntity(user)));
    }

    @Override
    public User update(User user) {
        return toDomain(repository.save(toJpaEntity(user)));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<User> findByNameContaining(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream().map(this::toDomain).toList();
    }

    private UserJpaEntity toJpaEntity(User user) {
        UserTypeJpaEntity userTypeEntity = null;
        if (user.getUserType() != null) {
            userTypeEntity = new UserTypeJpaEntity(user.getUserType().getId(), user.getUserType().getName());
        }
        return new UserJpaEntity(
                user.getId(), user.getName(), user.getEmail(), user.getLogin(),
                user.getPassword(), user.getAddress(), user.getLastModifiedDate(), userTypeEntity
        );
    }

    private User toDomain(UserJpaEntity entity) {
        UserType userType = null;
        if (entity.getUserType() != null) {
            userType = new UserType(entity.getUserType().getId(), entity.getUserType().getName());
        }
        return new User(
                entity.getId(), entity.getName(), entity.getEmail(), entity.getLogin(),
                entity.getPassword(), entity.getAddress(), entity.getLastModifiedDate(), userType
        );
    }
}
