package mx.edu.utez.model;
import java.util.Date;

public class Loan {
    private int loanId;
    private User user;
    private Inventory inventory;
    private Date loanDate;
    private Date dueDate;
    private Date returnDate;
    private String status;

    public Loan(int loanId, User user, Inventory inventory, Date loanDate, Date dueDate) {
        this.loanId = loanId;
        this.user = user;
        this.inventory = inventory;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = "pending";
        this.returnDate = null;
    }

    // Getters y Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public void markAsReturned() {
        this.status = "returned";
        this.returnDate = new Date();
    }

    public boolean isPending() {
        return "pending".equals(this.status);
    }
}

