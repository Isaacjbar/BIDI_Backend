package com.sibi.GestionDeBibliotecas.Inventario.Model;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "inventory")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;

    @Column(nullable = false)
    private Integer availableCopies;

    @Column(nullable = false)
    private Integer totalCopies;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Libro libro;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public Inventario() {

    }

    public Inventario(Integer inventoryId, Integer availableCopies, Integer totalCopies, Status status, Libro libro) {
        this.inventoryId = inventoryId;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.status = status;
        this.libro = libro;
    }

    public Inventario(Libro libro, Status status, Integer totalCopies, Integer availableCopies) {
        this.libro = libro;
        this.status = status;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}