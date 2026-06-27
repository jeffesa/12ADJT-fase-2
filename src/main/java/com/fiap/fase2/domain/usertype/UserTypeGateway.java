package com.fiap.fase2.domain.usertype;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeGateway {

    UserType create(UserType userType);

    UserType update(UserType userType);

    void delete(UUID id);

    Optional<UserType> findById(UUID id);

    Optional<UserType> findByName(String name);

    List<UserType> findAll();
}
