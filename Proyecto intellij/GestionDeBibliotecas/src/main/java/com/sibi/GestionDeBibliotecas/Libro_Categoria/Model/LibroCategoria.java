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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria categoria;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private java.util.Date createdAt = new java.util.Date();
}
