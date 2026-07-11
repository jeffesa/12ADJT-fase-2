package com.fiap.fase2.infra.persistence.user;

import com.fiap.fase2.infra.persistence.usertype.UserTypeJpaEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserTypeJpaEntity userType;

    public UserJpaEntity() {
    }

    public UserJpaEntity(UUID id, String name, String email, String login, String password,
                         String address, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.address = address;
        this.lastModifiedDate = lastModifiedDate;
    }

    public UserJpaEntity(UUID id, String name, String email, String login, String password,
                         String address, LocalDateTime lastModifiedDate, UserTypeJpaEntity userType) {
        this(id, name, email, login, password, address, lastModifiedDate);
        this.userType = userType;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }
    public UserTypeJpaEntity getUserType() { return userType; }
    public void setUserType(UserTypeJpaEntity userType) { this.userType = userType; }
}
