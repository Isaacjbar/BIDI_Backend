package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PrestamoDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Long prestamoId;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "El usuario no puede ser nulo")
    private Long usuarioId;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "El inventario no puede ser nulo")
    private Long inventarioId;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "La fecha de préstamo no puede ser nula")
    private java.util.Date fechaPrestamo;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "La fecha de vencimiento no puede ser nula")
    private java.util.Date fechaVencimiento;

    private java.util.Date fechaDevolucion;

    @NotBlank(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private String estado;

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}

    // Getters and Setters
    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Long inventarioId) {
        this.inventarioId = inventarioId;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}