package com.fiap.fase2.domain.usertype;

import java.util.UUID;

public class UserType {

    private UUID id;
    private String name;

    public UserType() {
    }

    public UserType(UUID id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do tipo de usuário é obrigatório");
        }
    }
}
