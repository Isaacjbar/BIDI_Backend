package com.sibi.GestionDeBibliotecas.Libro.Model;

import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaDTO;
import com.sibi.GestionDeBibliotecas.Libro.Model.Libro.Status;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class LibroDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Integer bookId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El título no puede estar vacío")
    private String title;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El autor no puede estar vacío")
    private String author;

    @NotNull(groups = {CambiarEstado.class}, message = "El estado no puede estar vacío")
    private Status status;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "La descripción no puede estar vacía")
    private String description;

    @NotEmpty(groups = {Registrar.class, Modificar.class}, message = "La lista de categorias no puede estar vacía")
    private List<CategoriaDTO> categorias; // Cambiado a CategoriaDTO en lugar de LibroCategoria
    // Constructors
    public LibroDTO() {}

    public LibroDTO(Integer bookId, String title, String author, String description, Status status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
    }

    public LibroDTO(String author, String title, String description, List<CategoriaDTO> categorias) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.categorias = categorias;
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> libroCategorias) {
        this.categorias = libroCategorias;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}
}
