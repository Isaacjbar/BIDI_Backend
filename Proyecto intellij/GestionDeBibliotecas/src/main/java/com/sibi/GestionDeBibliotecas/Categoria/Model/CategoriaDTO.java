package com.sibi.GestionDeBibliotecas.Categoria.Model;

import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriaDTO {
    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El id no puede ser nulo")
    private Integer categoryId;

    @NotBlank(groups = {Registrar.class, Modificar.class}, message = "El nombre de la categoría no puede estar vacío")
    private String categoryName;

    @NotNull(groups = {Modificar.class, CambiarEstado.class}, message = "El estado no puede estar vacío")
    private Status status;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Integer categoryId, String categoryName, Status status) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public interface Registrar {}
    public interface Modificar {}
    public interface CambiarEstado {}
}
