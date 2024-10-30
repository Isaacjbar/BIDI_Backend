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

    @Column(name = "category_name", columnDefinition = "VARCHAR(100)", nullable = false)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<LibroCategoria> libroCategorias;
}
