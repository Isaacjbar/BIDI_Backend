package mx.edu.utez.model;
import java.util.Date;

public class Loan {
    private String id;
    private Book book;
    private User user;
    private Date loanDate;
    private Date returnDate;
    private int quantity;

    public Loan(String id, Book book, User user, Date loanDate, int quantity) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.quantity = quantity;
        this.returnDate = null;  // Inicialmente no hay fecha de devolución
    }

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isActive() {
        return returnDate == null;  // Un préstamo está activo si no tiene fecha de devolución
    }

    public boolean isReturned() {
        return returnDate != null;  // Un préstamo está devuelto si tiene fecha de devolución
    }

    public int getQuantity() {
        return quantity;
    }
}
