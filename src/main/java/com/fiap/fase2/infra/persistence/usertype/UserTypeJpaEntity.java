package com.fiap.fase2.infra.persistence.usertype;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_types")
public class UserTypeJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public UserTypeJpaEntity() {
    }

    public UserTypeJpaEntity(UUID id, String name) {
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
        this.name = name;
    }
}
