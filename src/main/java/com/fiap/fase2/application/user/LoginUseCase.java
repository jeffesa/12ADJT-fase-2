package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.user.PasswordHasher;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;

public class LoginUseCase {

    private final UserGateway userGateway;
    private final PasswordHasher passwordHasher;

    public LoginUseCase(UserGateway userGateway, PasswordHasher passwordHasher) {
        this.userGateway = userGateway;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String login, String password) {
        User user = userGateway.findByLogin(login)
                .orElseThrow(() -> new BusinessException("Login ou senha inválidos"));

        if (!passwordHasher.matches(password, user.getPassword())) {
            throw new BusinessException("Login ou senha inválidos");
        }

        return user;
    }
}
