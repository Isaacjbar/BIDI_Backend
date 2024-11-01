package com.sibi.GestionDeBibliotecas.Security.Dto;

public class AuthRequest {
    private String correo;
    private String contrasena;

    public AuthRequest() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}