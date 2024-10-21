package mx.edu.utez.controller;

import mx.edu.utez.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookController {
    private List<Book> books = new ArrayList<>();

    // Crear Libro
    public String addBook(Book book) {
        // Validar que el título no esté repetido
        for (Book existingBook : books) {
            if (existingBook.getTitle().equals(book.getTitle())) {
                return "Este libro ya está registrado.";
            }
        }

        // Validar los campos obligatorios
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            return "El autor es un campo obligatorio.";
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return "El título es un campo obligatorio.";
        }
        if (book.getCategory() == null || book.getCategory().isEmpty()) {
            return "La categoría es un campo obligatorio.";
        }
        if (book.getStatus() == null || (!book.getStatus().equals("Activo") && !book.getStatus().equals("Inactivo"))) {
            return "El estado es un campo obligatorio (Activo o Inactivo).";
        }

        books.add(book);
        return "Libro agregado exitosamente.";
    }

    // Consultar libros por categoría
    public List<Book> getBooksByCategory(String category) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    // Consultar libro por title
    public Book getBookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    // Consultar libros por estado (Activo/Inactivo)
    public List<Book> getBooksByStatus(String status) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getStatus().equals(status)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    // Consultar todos los Libros
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Actualizar un libro
    public String updateBook(String title, Book updatedBook) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                // Validar los campos obligatorios
                if (updatedBook.getTitle() == null || updatedBook.getTitle().isEmpty()) {
                    return "El título es un campo obligatorio.";
                }
                if (updatedBook.getCategory() == null || updatedBook.getCategory().isEmpty()) {
                    return "La categoría es un campo obligatorio.";
                }
                if (updatedBook.getStatus() == null || (!updatedBook.getStatus().equals("Activo") && !updatedBook.getStatus().equals("Inactivo"))) {
                    return "El estado es un campo obligatorio (Activo o Inactivo).";
                }

                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setCategory(updatedBook.getCategory());
                book.setStatus(updatedBook.getStatus());
                book.setDescription(updatedBook.getDescription());

                return "Libro actualizado exitosamente.";
            }
        }
        return "Libro no encontrado.";
    }

    // Cambiar estado de un Libro
    public String deactivateBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                if ("Inactivo".equalsIgnoreCase(book.getStatus())) {
                    return "El libro ya está inactivo.";
                }
                book.setStatus("Inactivo");
                return "Libro desactivado exitosamente.";
            }
        }
        return "Libro no encontrado.";
    }
}