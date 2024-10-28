package mx.edu.utez.controller;

import mx.edu.utez.model.Book;
import mx.edu.utez.model.Inventory;
import mx.edu.utez.model.Loan;
import mx.edu.utez.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LoanController {

    List<Loan> loans = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Inventory> inventories = new ArrayList<>();

    public LoanController(InventoryController inventoryController) {

    }

    public LoanController() {
    }

    // Método para registrar un préstamo
    public String registerLoan(Loan loan) {
        // Verificar si el usuario está activo
        if (!loan.getUser().isActive()) {
            return "El usuario no está activo.";
        }

        // Buscar el inventario del libro
        Optional<Inventory> inventory = inventories.stream()
                .filter(inv -> inv.getBook().getTitle().equals(loan.getBook().getTitle()))
                .findFirst();

        if (inventory.isPresent()) {
            Inventory inv = inventory.get();

            // Verificar si el libro está activo y tiene copias disponibles
            if (!inv.getBook().getStatus().equals("active")) {
                return "No se puede prestar un libro inactivo.";
            }

            if (inv.getAvailableCopies() < loan.getQuantity()) {
                return "No hay suficientes ejemplares disponibles.";
            }

            // Registrar el préstamo y actualizar inventario
            loans.add(loan);
            for (int i = 0; i < loan.getQuantity(); i++) {
                try {
                    inv.borrowBook();
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
            return "Préstamo registrado exitosamente.";
        }
        return "Libro no encontrado en el inventario.";
    }

    // Método para obtener préstamos de un usuario
    public List<Loan> getLoansByUser(User user) {
        List<Loan> userLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getUser().getUserId() == user.getUserId()) {
                userLoans.add(loan);
            }
        }
        return userLoans;
    }

    // Método para obtener préstamos activos de un usuario
    public List<Loan> getActiveLoans(User user) {
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getUser().getUserId() == user.getUserId() && loan.isActive()) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    // Método para actualizar un préstamo (modificar la fecha de devolución)
    public String updateLoan(String loanId, Date newReturnDate) {
        for (Loan loan : loans) {
            if (loan.getId().equals(loanId)) {
                loan.setReturnDate(newReturnDate);
                return "Préstamo actualizado correctamente.";
            }
        }
        return "Préstamo no encontrado.";
    }

    // Método para actualizar el estado del préstamo a "devuelto"
    public String updateLoanStatus(String loanId, String newStatus) {
        for (Loan loan : loans) {
            if (loan.getId().equals(loanId)) {
                loan.setReturnDate(new Date()); // Marca la fecha de devolución como la actual

                // Buscar inventario para restaurar copia
                Inventory inv = inventories.stream()
                        .filter(i -> i.getBook().equals(loan.getBook()))
                        .findFirst()
                        .orElse(null);

                if (inv != null) {
                    inv.returnBook(); // Restaurar copia al inventario
                    return "Préstamo marcado como devuelto exitosamente.";
                }
                return "Libro no encontrado en el inventario.";
            }
        }
        return "Préstamo no encontrado.";
    }
}
