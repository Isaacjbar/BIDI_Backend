package com.sibi.GestionDeBibliotecas.Libro_Categoria.Model;

import jakarta.validation.constraints.NotNull;

public class LibroCategoriaDTO {
    @NotNull(groups = {Registrar.class, Modificar.class}, message = "El id del libro no puede ser nulo")
    private Integer libroId;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "El id de la categoría no puede ser nulo")
    private Integer categoriaId;

    public interface Registrar {}
    public interface Modificar {}
    public LibroCategoriaDTO() {
    }
    public LibroCategoriaDTO(Integer libroId, Integer categoriaId) {
        this.libroId = libroId;
        this.categoriaId = categoriaId;
    }

    public LibroCategoriaDTO(Integer libroId) {
        this.libroId = libroId;
    }

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }
}