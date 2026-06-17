package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.util.UUID;

public class DeleteUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public DeleteUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public void execute(UUID id) {
        userTypeGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado com id: " + id));

        userTypeGateway.delete(id);
    }
}
