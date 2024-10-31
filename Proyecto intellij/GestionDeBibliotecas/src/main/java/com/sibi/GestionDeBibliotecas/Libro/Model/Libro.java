package com.sibi.GestionDeBibliotecas.Libro.Model;

import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "book")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "author", columnDefinition = "VARCHAR(255)", nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "description", columnDefinition = "VARCHAR(250)")
    private String description;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LibroCategoria> libroCategorias;

    // Constructors
    public Libro() {}

    public Libro(String title, String author, String description, Status status) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
    }

    public Libro(Long bookId, String title, String author, String description, Status status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
    }
    public Libro(String author, String title, String description) {
        this.author = author;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
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

    public List<LibroCategoria> getLibroCategorias() {
        return libroCategorias;
    }

    public void setLibroCategorias(List<LibroCategoria> libroCategorias) {
        this.libroCategorias = libroCategorias;
    }
}