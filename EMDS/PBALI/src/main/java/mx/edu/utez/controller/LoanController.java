package mx.edu.utez.controller;

import mx.edu.utez.model.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanController {
    private ArrayList<Loan> loans = new ArrayList<>();
    private InventoryController inventoryController;

    public LoanController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    // Crear Préstamo
    public String addLoan(Loan loan) {
        try {
            loan.getInventory().borrowBook();
            loans.add(loan);
            return "Préstamo realizado exitosamente.";
        } catch (Exception e) {
            return "Error al realizar el préstamo: " + e.getMessage();
        }
    }

    // Consultar Préstamo por ID
    public Loan getLoanById(int loanId) throws Exception {
        return loans.stream()
                .filter(loan -> loan.getLoanId() == loanId && loan.isPending())
                .findFirst()
                .orElseThrow(() -> new Exception("Préstamo no encontrado o ya devuelto."));
    }

    // Consultar todos los Préstamos
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    // Consultar Préstamos por estado (pendiente/devuelto)
    public List<Loan> getLoansByStatus(String status) {
        List<Loan> filteredLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getStatus().equals(status)) {
                filteredLoans.add(loan);
            }
        }
        return filteredLoans;
    }

    // Actualizar Préstamo (marcar como devuelto)
    public String returnLoan(int loanId) throws Exception {
        Loan loan = getLoanById(loanId);
        loan.markAsReturned();
        loan.getInventory().returnBook();
        return "Préstamo devuelto exitosamente.";
    }

    // Eliminar (desactivar) Préstamo
    public String deactivateLoan(int loanId) throws Exception {
        Loan loan = getLoanById(loanId);
        loan.markAsReturned();
        return "Préstamo desactivado.";
    }
}
