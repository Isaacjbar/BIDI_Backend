package com.sibi.GestionDeBibliotecas.Libro.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LibroDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Long libroId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El autor no puede estar vacío")
    private String autor;

    @NotBlank(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private String estado;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "La descripción no puede estar vacía")
    private String descripcion;

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}
}
