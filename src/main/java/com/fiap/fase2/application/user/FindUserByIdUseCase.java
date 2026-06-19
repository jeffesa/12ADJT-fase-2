package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;

import java.util.UUID;

public class FindUserByIdUseCase {

    private final UserGateway userGateway;

    public FindUserByIdUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(UUID id) {
        return userGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));
    }
}
