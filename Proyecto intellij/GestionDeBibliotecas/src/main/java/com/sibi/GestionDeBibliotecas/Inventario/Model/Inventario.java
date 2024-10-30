package com.sibi.GestionDeBibliotecas.Inventario.Model;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "inventory")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventarioId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Libro libro;

    @Column(name = "available_copies", nullable = false)
    private int copiasDisponibles;

    @Column(name = "total_copies", nullable = false)
    private int copiasTotales;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Estado estado = Estado.ACTIVO;

    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prestamo> prestamos;
}