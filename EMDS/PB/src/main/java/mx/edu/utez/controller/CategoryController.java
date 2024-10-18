package mx.edu.utez.controller;

import mx.edu.utez.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private ArrayList<Category> categories = new ArrayList<>();

    // Crear Categoría
    public String addCategory(Category category) {
        // Verificar si el nombre de la categoría está vacío
        if (category.getCategoryName().isEmpty()) {
            return "El nombre de la categoría no puede estar vacío";
        }

        // Verificar si ya existe una categoría con el mismo nombre
        for (Category existingCategory : categories) {
            if (existingCategory.getCategoryName().equals(category.getCategoryName())) {
                return "La categoría ya existe";
            }
        }

        categories.add(category);
        return "Categoría agregada exitosamente";  // Quitar el punto al final
    }


    // Consultar Categoría por ID
    public Category getCategoryById(int categoryId) throws Exception {
        return categories.stream()
                .filter(category -> category.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new Exception("Categoría no encontrada."));
    }

    // Consultar todas las Categorías
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    // Consultar Categorías por estado (activo/inactivo)
    public List<Category> getCategoriesByStatus(String status) {
        List<Category> filteredCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category.getStatus().equals(status)) {
                filteredCategories.add(category);
            }
        }
        return filteredCategories;
    }

    // Actualizar Categoría
    public String updateCategory(int categoryId, Category updatedCategory) throws Exception {
        Category category = getCategoryById(categoryId);

        // Verificar si el nombre de la categoría ya existe en otra categoría
        for (Category existingCategory : categories) {
            if (existingCategory.getCategoryId() != categoryId &&
                    existingCategory.getCategoryName().equals(updatedCategory.getCategoryName())) {
                return "El nombre de la categoría ya está en uso.";
            }
        }

        // Actualizar el nombre de la categoría
        category.setCategoryName(updatedCategory.getCategoryName());
        return "Categoría actualizada exitosamente.";
    }




    // Eliminar (desactivar) Categoría
    public String deactivateCategory(int categoryId) throws Exception {
        Category category = getCategoryById(categoryId);

        // Si la categoría ya está inactiva, simplemente devuelve el mensaje
        if (!category.isActive()) {
            return "Categoría desactivada.";
        }

        // Cambiar el estado de la categoría a inactivo
        category.deactivate();
        return "Categoría desactivada.";
    }




}
