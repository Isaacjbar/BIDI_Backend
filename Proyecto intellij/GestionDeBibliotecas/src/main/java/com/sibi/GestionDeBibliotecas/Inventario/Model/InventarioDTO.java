package com.sibi.GestionDeBibliotecas.Inventario.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class InventarioDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Integer inventarioId;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "Las copias disponibles no pueden ser nulas")
    @PositiveOrZero(groups = {Registrar.class, Modificar.class}, message = "Las copias disponibles no pueden ser negativas")
    private int copiasDisponibles;

    @NotNull(groups = {Registrar.class, Modificar.class}, message = "Las copias totales no pueden ser nulas")
    @PositiveOrZero(groups = {Registrar.class, Modificar.class}, message = "Las copias totales no pueden ser negativas")
    private int copiasTotales;

    @NotBlank(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private String estado;

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}

    // Getters and Setters
    public Integer getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Integer inventarioId) {
        this.inventarioId = inventarioId;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
