package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.util.UUID;

public class UpdateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public UpdateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(UUID id, String name) {
        UserType existing = userTypeGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado com id: " + id));

        userTypeGateway.findByName(name)
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new BusinessException("Tipo de usuário já existe com nome: " + name);
                });

        existing.setName(name);
        return userTypeGateway.update(existing);
    }
}
