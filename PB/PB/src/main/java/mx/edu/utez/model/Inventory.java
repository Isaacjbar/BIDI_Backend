package mx.edu.utez.model;

public class Inventory {
    private int inventoryId;
    private Book book;
    private int availableCopies;
    private int totalCopies;
    private String status;

    public Inventory(int inventoryId, Book book, int availableCopies, int totalCopies) {
        this.inventoryId = inventoryId;
        this.book = book;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.status = "active";
    }

    // Getters y Setters

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void deactivate() {
        this.status = "inactive";
    }

    public boolean isActive() {
        return "active".equals(this.status);
    }

    // Métodos adicionales
    public void borrowBook() throws Exception {
        if (availableCopies <= 0) {
            throw new Exception("No hay copias disponibles para prestar.");
        }
        this.availableCopies--;
    }

    public void returnBook() {
        if (availableCopies < totalCopies) {
            this.availableCopies++;
        }
    }
}
