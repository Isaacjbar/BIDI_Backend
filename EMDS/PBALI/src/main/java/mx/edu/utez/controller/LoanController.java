package mx.edu.utez.controller;

import mx.edu.utez.model.Book;
import mx.edu.utez.model.Loan;
import mx.edu.utez.model.User;

import java.util.ArrayList;
import java.util.List;

public class LoanController {

    private List<Loan> loans = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    // Registrar un préstamo
    public String registerLoan(String loanId, String bookId, String userId, int quantity) {
        Book book = findBookById(bookId);
        User user = findUserById(userId);

        if (book == null) {
            return "Libro no encontrado.";
        }
        if (user == null) {
            return "Usuario no encontrado.";
        }
        if (book.getStatus().equals("Inactivo")) {
            return "No se puede prestar un libro inactivo.";
        }
        if (book.getAvailableCopies() < quantity) {
            return "No hay suficientes ejemplares disponibles.";
        }

        // Registrar el préstamo
        Loan loan = new Loan(loanId, book, user, new Date(), quantity);
        loans.add(loan);

        // Restar la cantidad de ejemplares disponibles
        book.setAvailableCopies(book.getAvailableCopies() - quantity);

        return "Préstamo registrado exitosamente.";
    }

    // Consultar préstamos por usuario
    public List<Loan> getLoansByUser(String userId) {
        User user = findUserById(userId);

        if (user == null) {
            return null;
        }

        List<Loan> userLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getUser().getId().equals(userId)) {
                userLoans.add(loan);
            }
        }

        return userLoans;
    }

    // Consultar préstamos activos
    public List<Loan> getActiveLoans(String userId) {
        User user = findUserById(userId);

        if (user == null) {
            return null;
        }

        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getUser().getId().equals(userId) && loan.isActive()) {
                activeLoans.add(loan);
            }
        }

        return activeLoans;
    }

    // Actualizar un préstamo
    public String updateLoan(String loanId, Date newReturnDate) {
        Loan loan = findLoanById(loanId);

        if (loan == null) {
            return "Préstamo no encontrado.";
        }

        if (loan.getBook().getState().equals("Inactivo")) {
            return "No se puede actualizar un préstamo para un libro inactivo.";
        }

        loan.setReturnDate(newReturnDate);
        return "Préstamo actualizado correctamente.";
    }

    // Método auxiliar para buscar un libro por ID
    private Book findBookById(String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    // Método auxiliar para buscar un usuario por ID
    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    // Método auxiliar para buscar un préstamo por ID
    private Loan findLoanById(String loanId) {
        for (Loan loan : loans) {
            if (loan.getId().equals(loanId)) {
                return loan;
            }
        }
        return null;
    }
}
