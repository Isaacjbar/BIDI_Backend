package com.sibi.GestionDeBibliotecas.Libro.Model;

import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;
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
}
