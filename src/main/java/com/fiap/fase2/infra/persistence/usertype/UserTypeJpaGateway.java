package com.fiap.fase2.infra.persistence.usertype;

import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserTypeJpaGateway implements UserTypeGateway {

    private final UserTypeRepository repository;

    public UserTypeJpaGateway(UserTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserType create(UserType userType) {
        UserTypeJpaEntity entity = toJpaEntity(userType);
        UserTypeJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public UserType update(UserType userType) {
        UserTypeJpaEntity entity = toJpaEntity(userType);
        UserTypeJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<UserType> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        return repository.findByName(name).map(this::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private UserTypeJpaEntity toJpaEntity(UserType userType) {
        return new UserTypeJpaEntity(userType.getId(), userType.getName());
    }

    private UserType toDomain(UserTypeJpaEntity entity) {
        return new UserType(entity.getId(), entity.getName());
    }
}
