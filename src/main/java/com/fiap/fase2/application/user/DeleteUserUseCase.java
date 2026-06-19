package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.UserGateway;

import java.util.UUID;

public class DeleteUserUseCase {

    private final UserGateway userGateway;

    public DeleteUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(UUID id) {
        userGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));

        userGateway.delete(id);
    }
}
