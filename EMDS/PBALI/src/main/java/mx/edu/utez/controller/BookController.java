package mx.edu.utez.controller;

import mx.edu.utez.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookController {
    private ArrayList<Book> books = new ArrayList<>();

    // Crear Libro
    public String addBook(Book book) {
        if (!book.isIsbnValid()) {
            return "ISBN inválido.";
        }
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
