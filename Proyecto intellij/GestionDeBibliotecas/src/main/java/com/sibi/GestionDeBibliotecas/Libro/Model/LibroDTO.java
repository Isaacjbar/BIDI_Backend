package com.sibi.GestionDeBibliotecas.Libro.Model;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LibroDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Integer libroId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El título no puede estar vacío")
    private String title;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El autor no puede estar vacío")
    private String author;

    @NotNull(groups = {CambiarEstado.class}, message = "El estado no puede estar vacío")
    private Status status;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "La descripción no puede estar vacía")
    private String description;

    // Constructors
    public LibroDTO() {}

    public LibroDTO(Integer libroId, String title, String author, String description, Status status) {
        this.libroId = libroId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
    }

    public LibroDTO(String author, String title, String description) {
        this.author = author;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
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
