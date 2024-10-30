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
    private Integer inventarioId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Libro libro;

    @Column(name = "available_copies", nullable = false)
    private int copiasDisponibles;

    @Column(name = "total_copies", nullable = false)
    private int copiasTotales;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Inventario.Status status = Inventario.Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prestamo> prestamos;

    // Constructors
    public Inventario() {}

    public Inventario(Integer inventarioId, Libro libro, int copiasDisponibles, int copiasTotales, Status status, List<Prestamo> prestamos) {
        this.inventarioId = inventarioId;
        this.libro = libro;
        this.copiasDisponibles = copiasDisponibles;
        this.copiasTotales = copiasTotales;
        this.status = status;
        this.prestamos = prestamos;
    }

    public Inventario(Libro libro, int copiasDisponibles, int copiasTotales, Status status, List<Prestamo> prestamos) {
        this.libro = libro;
        this.copiasDisponibles = copiasDisponibles;
        this.copiasTotales = copiasTotales;
        this.status = status;
        this.prestamos = prestamos;
    }

    // Getters and Setters
    public Integer getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Integer inventarioId) {
        this.inventarioId = inventarioId;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public int getCopiasDisponibles() {
        return copiasDisponibles;
    }

    public void setCopiasDisponibles(int copiasDisponibles) {
        this.copiasDisponibles = copiasDisponibles;
    }

    public int getCopiasTotales() {
        return copiasTotales;
    }

    public void setCopiasTotales(int copiasTotales) {
        this.copiasTotales = copiasTotales;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}