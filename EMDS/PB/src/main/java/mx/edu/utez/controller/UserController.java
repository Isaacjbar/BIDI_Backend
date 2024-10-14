package mx.edu.utez.controller;

import mx.edu.utez.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private ArrayList<User> users = new ArrayList<>();

    // Crear Usuario
    public String addUser(User user) {
        if (!user.isEmailValid()) {
            return "Email inválido.";
        }
        users.add(user);
        return "Usuario agregado exitosamente.";
    }

    // Consultar Usuario por ID
    public User getUserById(int userId) throws Exception {
        return users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst()
                .orElseThrow(() -> new Exception("Usuario no encontrado."));
    }

    // Consultar todos los Usuarios
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Consultar Usuarios por estado (activo/inactivo)
    public List<User> getUsersByStatus(String status) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getStatus().equals(status)) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    // Actualizar Usuario
    public String updateUser(int userId, User updatedUser) throws Exception {
        User user = getUserById(userId);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        return "Usuario actualizado exitosamente.";
    }

    // Eliminar Usuario (cambiar estado a inactivo)
    public String deactivateUser(int userId) throws Exception {
        User user = getUserById(userId);
        user.deactivate();
        return "Usuario desactivado.";
    }
}
