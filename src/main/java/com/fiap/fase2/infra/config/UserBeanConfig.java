package com.fiap.fase2.infra.config;

import com.fiap.fase2.application.user.*;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserBeanConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway, PasswordEncoder passwordEncoder) {
        return new CreateUserUseCase(userGateway, userTypeGateway, passwordEncoder);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        return new UpdateUserUseCase(userGateway, userTypeGateway);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserGateway userGateway) {
        return new FindUserByIdUseCase(userGateway);
    }

    @Bean
    public FindAllUsersUseCase findAllUsersUseCase(UserGateway userGateway) {
        return new FindAllUsersUseCase(userGateway);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        return new ChangePasswordUseCase(userGateway, passwordEncoder);
    }

    @Bean
    public LoginUseCase loginUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        return new LoginUseCase(userGateway, passwordEncoder);
    }
}
