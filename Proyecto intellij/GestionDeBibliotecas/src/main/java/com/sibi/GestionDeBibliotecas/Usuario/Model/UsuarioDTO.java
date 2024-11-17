package com.sibi.GestionDeBibliotecas.Usuario.Model;

import jakarta.validation.constraints.*;

import java.sql.Date;

public class UsuarioDTO {

    @NotNull(groups = {Modificar.class, CambiarEstado.class, Consultar.class, Recuperacion.class}, message = "Error con el identificador de usuario")
    private Long usuarioId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(groups = {Registrar.class, Modificar.class, Solicitud.class}, message = "El correo no puede estar vacío")
    @Email(groups = {Registrar.class, Modificar.class, Solicitud.class}, message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(groups = {Registrar.class, Modificar.class, Validacion.class, Recuperacion.class}, message = "La contraseña no puede estar vacía")
    private String contrasena;

    private String rol;
    private String estado;
    private String numeroTelefono;

    @NotBlank(groups = {Validacion.class}, message = "El código no puede estar vacío")
    @Max(value = 6, groups = {Validacion.class}, message = "El código no debe exceder el número de caracteres")
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public interface Solicitud {
    }

    public interface Validacion {
    }

    public interface Recuperacion {
    }

    public interface Consultar {
    }
}