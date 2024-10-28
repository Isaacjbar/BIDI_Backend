package mx.edu.utez.controller;

import mx.edu.utez.model.Book;
import mx.edu.utez.model.Inventory;
import mx.edu.utez.model.Loan;
import mx.edu.utez.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LoanControllerTest {

    private LoanController loanController;
    private Inventory inventory;
    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        loanController = new LoanController();

        // Datos de prueba comunes
        user = new User(1, "John", "Doe", "john@example.com", "123456789", "password", "USER");
        book = new Book("Gabriel Garcia Marquez", "Cien años de soledad", "Novela", "active", "Una novela clásica");
        inventory = new Inventory(1, book, 5, 5);

        // Inicialización de datos de prueba en el controlador
        loanController.inventories.add(inventory);
        loanController.users.add(user);
    }

    @Test
        // CPM401-1
    void testRegisterLoan_Success() {
        Loan loan = new Loan("1", book, user, new Date(), 1);
        String result = loanController.registerLoan(loan);

        assertEquals("Préstamo registrado exitosamente.", result, "El préstamo debería registrarse correctamente.");
    }

    @Test
        // CPM401-2
    void testRegisterLoan_BookInactive() {
        book.setStatus("inactive");
        Loan loan = new Loan("2", book, user, new Date(), 1);
        String result = loanController.registerLoan(loan);

        assertEquals("No se puede prestar un libro inactivo.", result, "El préstamo no debería registrarse si el libro está inactivo.");
    }

    @Test
        // CPM401-3
    void testRegisterLoan_InsufficientCopies() {
        Loan loan = new Loan("3", book, user, new Date(), 6); // Más de las copias disponibles
        String result = loanController.registerLoan(loan);

        assertEquals("No hay suficientes ejemplares disponibles.", result, "El préstamo no debería registrarse si no hay copias suficientes.");
    }

    @Test
        // CPM40402-1
    void testGetLoansByUser_Success() {
        Loan loan = new Loan("4", book, user, new Date(), 1);
        loanController.registerLoan(loan);

        assertEquals(1, loanController.getLoansByUser(user).size(), "Debería haber un préstamo registrado para el usuario.");
    }

    @Test
        // CPM402-2
    void testGetActiveLoans_WithActiveLoans() {
        Loan loan = new Loan("5", book, user, new Date(), 1);
        loanController.registerLoan(loan);

        assertEquals(1, loanController.getActiveLoans(user).size(), "Debería haber un préstamo activo para el usuario.");
    }

    @Test
        // CPM402-3
    void testGetActiveLoans_NoActiveLoans() {
        assertEquals(0, loanController.getActiveLoans(user).size(), "No debería haber préstamos activos para el usuario.");
    }

    @Test
        // CPM403-1
    void testUpdateLoan_Success() {
        Loan loan = new Loan("6", book, user, new Date(), 1);
        loanController.registerLoan(loan);

        String result = loanController.updateLoan("6", new Date());
        assertEquals("Préstamo actualizado correctamente.", result, "El préstamo debería actualizarse correctamente.");
    }

    @Test
        // CPM403-2
    void testUpdateLoan_NonExistentLoan() {
        String result = loanController.updateLoan("99999", new Date());
        assertEquals("Préstamo no encontrado.", result, "El préstamo no debería actualizarse si no existe.");
    }

    @Test
        // CPM403-3
    void testUpdateLoanStatus_Success() {
        Loan loan = new Loan("7", book, user, new Date(), 1);
        loanController.registerLoan(loan);

        String result = loanController.updateLoanStatus("7", "devuelto");
        assertEquals("Préstamo marcado como devuelto exitosamente.", result, "El préstamo debería marcarse como devuelto.");
        assertEquals(5, inventory.getAvailableCopies(), "La cantidad de copias disponibles debería aumentar en 1.");
    }
}
