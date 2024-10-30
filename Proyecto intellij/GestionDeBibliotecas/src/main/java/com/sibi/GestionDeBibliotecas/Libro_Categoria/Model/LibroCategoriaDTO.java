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

    public LibroCategoriaDTO() {
    }
    public LibroCategoriaDTO(Long libroId, Long categoriaId) {
        this.libroId = libroId;
        this.categoriaId = categoriaId;
    }

    public LibroCategoriaDTO(Long libroId) {
        this.libroId = libroId;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}