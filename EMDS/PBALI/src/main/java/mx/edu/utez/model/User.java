package mx.edu.utez.model;

import java.util.Date;

public class User {
    private int userId;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String status;
    private String code;
    private Date codeGeneratedAt;



    // Constructor
    public User(int userId, String name, String lastName, String email, String phone, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = "active"; // Por defecto, el estado es 'active'
        this.code = null;
        this.codeGeneratedAt = null;

    }


    // Getters y Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCodeGeneratedAt() {
        return codeGeneratedAt;
    }

    public void setCodeGeneratedAt(Date codeGeneratedAt) {
        this.codeGeneratedAt = codeGeneratedAt;
    }

    // Método para cambiar el estado (eliminar)
    public void desactivate() {
        this.status = "inactive";
    }

    public boolean isActive() {
        return "active".equals(this.status);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Validaciones
    public boolean isEmailValid() {
        return this.email != null && this.email.contains("@");
    }


}
