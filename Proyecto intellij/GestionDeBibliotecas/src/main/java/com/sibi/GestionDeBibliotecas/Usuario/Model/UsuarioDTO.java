package com.sibi.GestionDeBibliotecas.Usuario.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class UsuarioDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Long usuarioId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El correo no puede estar vacío")
    private String correo;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "La contraseña no puede estar vacía")
    private String contrasena;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "Hubo un creando tus datos, intenta de nuevo")
    private String rol;

    @NotBlank(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private String estado;

    private String numeroTelefono;
    private String codigo;
    private Date codigoGeneradoEn;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getCodigoGeneradoEn() {
        return codigoGeneradoEn;
    }

    public void setCodigoGeneradoEn(Date codigoGeneradoEn) {
        this.codigoGeneradoEn = codigoGeneradoEn;
    }

    public interface Registrar {
    }

    public interface Modificar {
    }

    public interface CambiarEstado {
    }
}