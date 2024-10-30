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
}
