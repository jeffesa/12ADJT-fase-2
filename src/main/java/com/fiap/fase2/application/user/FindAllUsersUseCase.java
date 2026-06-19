package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;

import java.util.List;

public class FindAllUsersUseCase {

    private final UserGateway userGateway;

    public FindAllUsersUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> execute(String name) {
        if (name == null || name.isBlank()) {
            return userGateway.findAll();
        }
        return userGateway.findByNameContaining(name.trim());
    }
}
