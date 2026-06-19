package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public UpdateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    public User execute(UUID id, String name, String email, String login,
                        String address, UUID userTypeId) {
        User existing = userGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));

        userGateway.findByEmail(email)
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new BusinessException("Email já cadastrado: " + email); });

        userGateway.findByLogin(login)
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new BusinessException("Login já cadastrado: " + login); });

        UserType userType = userTypeGateway.findById(userTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado com id: " + userTypeId));

        existing.setName(name);
        existing.setEmail(email);
        existing.setLogin(login);
        existing.setAddress(address);
        existing.setLastModifiedDate(LocalDateTime.now());
        existing.setUserType(userType);

        return userGateway.update(existing);
    }
}
