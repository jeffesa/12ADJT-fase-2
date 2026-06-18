package com.fiap.fase2.infra.config;

import com.fiap.fase2.application.usertype.*;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeBeanConfig {

    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase(UserTypeGateway gateway) {
        return new CreateUserTypeUseCase(gateway);
    }

    @Bean
    public UpdateUserTypeUseCase updateUserTypeUseCase(UserTypeGateway gateway) {
        return new UpdateUserTypeUseCase(gateway);
    }

    @Bean
    public DeleteUserTypeUseCase deleteUserTypeUseCase(UserTypeGateway gateway) {
        return new DeleteUserTypeUseCase(gateway);
    }

    @Bean
    public FindUserTypeByIdUseCase findUserTypeByIdUseCase(UserTypeGateway gateway) {
        return new FindUserTypeByIdUseCase(gateway);
    }

    @Bean
    public FindAllUserTypesUseCase findAllUserTypesUseCase(UserTypeGateway gateway) {
        return new FindAllUserTypesUseCase(gateway);
    }
}
