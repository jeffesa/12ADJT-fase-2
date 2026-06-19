package com.fiap.fase2.domain.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

    User create(User user);

    User update(User user);

    void delete(UUID id);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    List<User> findByNameContaining(String name);
}
