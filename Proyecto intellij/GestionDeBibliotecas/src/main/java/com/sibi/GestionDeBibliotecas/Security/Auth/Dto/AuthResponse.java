package com.sibi.GestionDeBibliotecas.Security.Auth.Dto;

import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;

public class AuthResponse {
    private String jwt;
    private Long userId;
    private String email;
    private long expiration;
    private Rol role;

    public AuthResponse(String jwt, Long userId, String email, long expiration, Rol role) {
        this.jwt = jwt;
        this.userId = userId;
        this.email = email;
        this.expiration = expiration;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public Rol getRole() {
        return role;
    }

    public void setRole(Rol role) {
        this.role = role;
    }
}