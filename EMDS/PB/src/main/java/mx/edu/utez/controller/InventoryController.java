package mx.edu.utez.controller;

import mx.edu.utez.model.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    private ArrayList<Inventory> inventories = new ArrayList<>();

    // Crear Inventario
    public String addInventory(Inventory inventory) {
        inventories.add(inventory);
        return "Inventario agregado exitosamente.";
    }

    // Consultar Inventario por ID
    public Inventory getInventoryById(int inventoryId) throws Exception {
        return inventories.stream()
                .filter(inventory -> inventory.getInventoryId() == inventoryId)
                .findFirst()
                .orElseThrow(() -> new Exception("Inventario no encontrado."));
    }

    // Consultar todo el Inventario
    public List<Inventory> getAllInventories() {
        return new ArrayList<>(inventories);
    }

    // Consultar Inventario por estado (activo/inactivo)
    public List<Inventory> getInventoriesByStatus(String status) {
        List<Inventory> filteredInventories = new ArrayList<>();
        for (Inventory inventory : inventories) {
            if (inventory.getStatus().equals(status)) {
                filteredInventories.add(inventory);
            }
        }
        return filteredInventories;
    }

    // Actualizar Inventario (disponibilidad de copias)
    public String updateInventory(int inventoryId, int availableCopies) throws Exception {
        Inventory inventory = getInventoryById(inventoryId);
        if (availableCopies > inventory.getTotalCopies()) {
            return "La cantidad disponible no puede exceder el total de copias.";
        }
        inventory.setAvailableCopies(availableCopies);
        return "Inventario actualizado exitosamente.";
    }

    // Eliminar (desactivar) Inventario
    public String deactivateInventory(int inventoryId) throws Exception {
        Inventory inventory = getInventoryById(inventoryId);
        inventory.deactivate();
        return "Inventario desactivado.";
    }
}
