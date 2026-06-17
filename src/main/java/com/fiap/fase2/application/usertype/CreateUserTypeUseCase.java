package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.util.UUID;

public class CreateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public CreateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(String name) {
        UserType userType = new UserType(UUID.randomUUID(), name);
        return userTypeGateway.create(userType);
    }
}
