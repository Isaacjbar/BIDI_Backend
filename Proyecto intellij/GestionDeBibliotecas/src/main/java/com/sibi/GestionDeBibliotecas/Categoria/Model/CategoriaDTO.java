package com.sibi.GestionDeBibliotecas.Categoria.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriaDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Long categoriaId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El nombre de la categoría no puede estar vacío")
    private String nombreCategoria;

    @NotBlank(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private String estado;

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}
}
