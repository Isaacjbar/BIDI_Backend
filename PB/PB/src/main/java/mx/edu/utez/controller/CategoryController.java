package mx.edu.utez.controller;

import mx.edu.utez.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private ArrayList<Category> categories = new ArrayList<>();

    // Crear Categoría
    public String addCategory(Category category) {
        categories.add(category);
        return "Categoría agregada exitosamente.";
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
        category.setCategoryName(updatedCategory.getCategoryName());
        return "Categoría actualizada exitosamente.";
    }

    // Eliminar (desactivar) Categoría
    public String deactivateCategory(int categoryId) throws Exception {
        Category category = getCategoryById(categoryId);
        category.deactivate();
        return "Categoría desactivada.";
    }
}
