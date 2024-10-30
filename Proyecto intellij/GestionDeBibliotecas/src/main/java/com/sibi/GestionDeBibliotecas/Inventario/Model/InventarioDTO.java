package com.sibi.GestionDeBibliotecas.Inventario.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class InventarioDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Long inventarioId;

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
}