package com.sibi.GestionDeBibliotecas.Libro.Model;

import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    public Libro() {

    }

    public Libro(Integer bookId, String title, String author, Status status, String description, Set<Categoria> categorias) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.status = status;
        this.description = description;
        this.categorias = categorias;
    }

    public Libro(String title, String author, Status status, String description, Set<Categoria> categorias) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.description = description;
        this.categorias = categorias;
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

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }
}