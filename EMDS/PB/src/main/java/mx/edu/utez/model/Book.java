package mx.edu.utez.model;

public class Book {
    private int bookId;
    private String author;
    private String title;
    private String category;
    private String status;  // Activo o Inactivo
    private String description;  // Opcional

    public Book(String author, String title, String category, String status, String description) {
        this.author = author;
        this.title = title;
        this.category = category;
        this.status = status;
        this.description = description;
    }

    // Getters y Setters

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}