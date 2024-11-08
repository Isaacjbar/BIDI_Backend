package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import jakarta.persistence.*;

@Entity
@Table(name = "loan")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Integer prestamoId;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private Libro libro;

    @Column(name = "loan_date", nullable = false)
    private java.util.Date fechaPrestamo;

    @Column(name = "due_date", nullable = false)
    private java.util.Date fechaVencimiento;

    @Column(name = "return_date")
    private java.util.Date fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Prestamo.Status status = Prestamo.Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    // Constructors
    public Prestamo() {}

    public Prestamo(Usuario usuario, Libro libro, java.util.Date fechaPrestamo, java.util.Date fechaVencimiento) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
    }

    // Getters and Setters
    public Integer getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Integer prestamoId) {
        this.prestamoId = prestamoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public java.util.Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(java.util.Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public java.util.Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(java.util.Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public java.util.Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(java.util.Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
