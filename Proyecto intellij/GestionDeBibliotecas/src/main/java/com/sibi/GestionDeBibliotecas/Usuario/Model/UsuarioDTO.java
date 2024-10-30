package com.sibi.GestionDeBibliotecas.Usuario.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Long usuarioId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El correo no puede estar vacío")
    private String correo;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "La contraseña no puede estar vacía")
    private String contrasena;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El rol no puede estar vacío")
    private String rol;

    @NotBlank(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private String estado;

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}
}