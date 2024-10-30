package com.sibi.GestionDeBibliotecas.Libro_Categoria.Model;

import jakarta.validation.constraints.NotNull;

public class LibroCategoriaDTO {
    @NotNull(groups = {Modificar.class}, message = "El id del libro no puede ser nulo")
    private Long libroId;

    @NotNull(groups = {Modificar.class}, message = "El id de la categoría no puede ser nulo")
    private Long categoriaId;

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}
}