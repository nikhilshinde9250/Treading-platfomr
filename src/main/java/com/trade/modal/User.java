package com.trade.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trade.domain.USER_ROLE;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "userdetail")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    @Enumerated(EnumType.STRING)
    @NotNull
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    // No-args constructor
    public User() {
    }

    // All-args constructor
    public User(long id, String fullName, String email, String password, TwoFactorAuth twoFactorAuth, USER_ROLE role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.twoFactorAuth = twoFactorAuth;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TwoFactorAuth getTwoFactorAuth() {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(TwoFactorAuth twoFactorAuth) {
        this.twoFactorAuth = twoFactorAuth;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }
}
