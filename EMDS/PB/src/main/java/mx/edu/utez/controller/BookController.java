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

        // Agregar el libro a la lista
        books.add(book);
        return "Libro agregado exitosamente.";
    }

    // Consultar Libro por ID
    public Book getBookById(int bookId) throws Exception {
        return books.stream()
                .filter(book -> book.getBookId() == bookId)
                .findFirst()
                .orElseThrow(() -> new Exception("Libro no encontrado."));
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

    // Actualizar Libro
    public String updateBook(int bookId, Book updatedBook) throws Exception {
        Book book = getBookById(bookId);

        // Actualizar campos
        book.setAuthor(updatedBook.getAuthor());
        book.setTitle(updatedBook.getTitle());
        book.setCategory(updatedBook.getCategory());
        book.setStatus(updatedBook.getStatus());
        book.setDescription(updatedBook.getDescription());  // Campo opcional

        return "Libro actualizado exitosamente.";
    }

    // Cambiar estado de un Libro (Desactivar)
    public String deactivateBook(int bookId) throws Exception {
        Book book = getBookById(bookId);
        book.setStatus("Inactivo");
        return "Libro desactivado.";
    }
}
