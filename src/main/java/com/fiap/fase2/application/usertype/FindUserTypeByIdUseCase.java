package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.util.UUID;

public class FindUserTypeByIdUseCase {

    private final UserTypeGateway userTypeGateway;

    public FindUserTypeByIdUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(UUID id) {
        return userTypeGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado com id: " + id));
    }
}
