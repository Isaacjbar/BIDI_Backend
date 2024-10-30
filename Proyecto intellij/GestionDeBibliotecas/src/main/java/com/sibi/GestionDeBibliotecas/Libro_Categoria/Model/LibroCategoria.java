package com.sibi.GestionDeBibliotecas.Libro_Categoria.Model;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;
import jakarta.persistence.*;

@Entity
@Table(name = "book_category")
public class LibroCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria categoria;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private java.util.Date createdAt = new java.util.Date();

    // Constructors
    public LibroCategoria() {}

    public LibroCategoria(Libro libro, Categoria categoria) {
        this.libro = libro;
        this.categoria = categoria;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }
}