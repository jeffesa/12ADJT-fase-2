package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;

import java.util.List;

public class FindAllUserTypesUseCase {

    private final UserTypeGateway userTypeGateway;

    public FindAllUserTypesUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public List<UserType> execute() {
        return userTypeGateway.findAll();
    }
}
