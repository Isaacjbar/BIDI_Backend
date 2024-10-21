package mx.edu.utez.controller;

import mx.edu.utez.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookController {
    private ArrayList<Book> books = new ArrayList<>();

    // Crear Libro
    public String addBook(Book book) {
        // Validar que el ISBN no esté repetido
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                return "El ISBN ya está registrado.";
            }
        }

        // Validar si el ISBN es correcto (13 caracteres)
        if (!book.isIsbnValid()) {
            return "ISBN inválido.";
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return "El título es un campo obligatorio.";
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            return "El autor es un campo obligatorio.";
        }
        if (book.getPublisher() == null || book.getPublisher().isEmpty()) {
            return "El editor es un campo obligatorio.";
        }
        if (book.getPublicationDate() == null) {
            return "La fecha de publicación es un campo obligatorio.";
        }
        if (book.getEdition() == null || book.getEdition().isEmpty()) {
            return "La edición es un campo obligatorio.";
        }
        if (book.getNumberOfPages() <= 0) {
            return "El número de páginas debe ser mayor que cero.";
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

    // Consultar todos los Libros
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Consultar Libros por estado (activo/inactivo)
    public List<Book> getBooksByStatus(String status) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getStatus().equals(status)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    // Actualizar Libro
    public String updateBook(int bookId, Book updatedBook) throws Exception {
        Book book = getBookById(bookId);
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        return "Libro actualizado exitosamente.";
    }

    // Eliminar Libro (cambiar estado a inactivo)
    public String deactivateBook(int bookId) throws Exception {
        Book book = getBookById(bookId);
        book.deactivate();
        return "Libro desactivado.";
    }
}