package com.sibi.GestionDeBibliotecas.Usuario.Model;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role = Role.CLIENT;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Status status = Status.ACTIVE;

    @Column
    private String code;

    @Column(name = "code_generated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp codeGeneratedAt;

    public enum Role {
        ADMINISTRATOR,
        CLIENT,
        GUEST
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public Usuario() {

    }

    public Usuario(Integer userId, String name, String email, String password, Role role, Status status, String code, Timestamp codeGeneratedAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.code = code;
        this.codeGeneratedAt = codeGeneratedAt;
    }

    public Usuario(String name, String email, String password, Role role, Status status, String code, Timestamp codeGeneratedAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.code = code;
        this.codeGeneratedAt = codeGeneratedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getCodeGeneratedAt() {
        return codeGeneratedAt;
    }

    public void setCodeGeneratedAt(Timestamp codeGeneratedAt) {
        this.codeGeneratedAt = codeGeneratedAt;
    }
}