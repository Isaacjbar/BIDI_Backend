package com.sibi.GestionDeBibliotecas.Categoria.Model;

import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "category")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "categoryName", columnDefinition = "VARCHAR(100)", nullable = false)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<LibroCategoria> libroCategorias;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor que inicializa el nombre y estado de la categoría
    public Categoria(String categoryName) {
        this.categoryName = categoryName;
    }

    // Constructor adicional para todos los campos
    public Categoria(Long categoryId, String categoryName, Status status, List<LibroCategoria> libroCategorias) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
        this.libroCategorias = libroCategorias;
    }

    // Getters y Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<LibroCategoria> getLibroCategorias() {
        return libroCategorias;
    }

    public void setLibroCategorias(List<LibroCategoria> libroCategorias) {
        this.libroCategorias = libroCategorias;
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
