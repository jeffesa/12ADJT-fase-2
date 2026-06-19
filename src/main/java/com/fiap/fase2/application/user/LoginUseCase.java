package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public LoginUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String login, String password) {
        User user = userGateway.findByLogin(login)
                .orElseThrow(() -> new BusinessException("Login ou senha inválidos"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("Login ou senha inválidos");
        }

        return user;
    }
}
