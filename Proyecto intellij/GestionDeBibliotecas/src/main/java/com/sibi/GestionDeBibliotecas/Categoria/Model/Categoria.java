package com.sibi.GestionDeBibliotecas.Categoria.Model;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false)
    private String category_name;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Categoria.Status status = Categoria.Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @ManyToMany(mappedBy = "categorias")
    private Set<Libro> libros = new HashSet<>();

    public Categoria() {

    }

    public Categoria(Integer categoryId, String category_name, Status status, Set<Libro> libros) {
        this.categoryId = categoryId;
        this.category_name = category_name;
        this.status = status;
        this.libros = libros;
    }

    public Categoria(String category_name, Status status, Set<Libro> libros) {
        this.category_name = category_name;
        this.status = status;
        this.libros = libros;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String name) {
        this.category_name = category_name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }
}