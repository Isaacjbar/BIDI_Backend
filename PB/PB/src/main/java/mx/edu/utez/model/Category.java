package mx.edu.utez.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private String status;

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = "active";
    }

    // Getters y Setters

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}

