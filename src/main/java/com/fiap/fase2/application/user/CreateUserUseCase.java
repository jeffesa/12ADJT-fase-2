package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.PasswordHasher;
import com.fiap.fase2.domain.user.PasswordValidator;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;
    private final PasswordHasher passwordHasher;

    public CreateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway,
                             PasswordHasher passwordHasher) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String name, String email, String login, String password,
                        String address, UUID userTypeId) {
        PasswordValidator.validate(password);

        userGateway.findByEmail(email).ifPresent(u -> {
            throw new BusinessException("Email já cadastrado: " + email);
        });

        userGateway.findByLogin(login).ifPresent(u -> {
            throw new BusinessException("Login já cadastrado: " + login);
        });

        UserType userType = userTypeGateway.findById(userTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado com id: " + userTypeId));

        User user = new User(
                UUID.randomUUID(),
                name,
                email,
                login,
                passwordHasher.encode(password),
                address,
                LocalDateTime.now(),
                userType
        );

        return userGateway.create(user);
    }
}
