package mx.edu.utez.controller;

import mx.edu.utez.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserController {
    private ArrayList<User> users = new ArrayList<>();

    // Crear Usuario
    public String addUser(String name, String email, String password, String confirmPassword) {
        // Validar que el correo no esté en uso
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return "Correo en uso";
            }
        }
        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            return "Contraseñas diferentes";
        }

        // Validar formato de correo
        if (email == null || !email.contains("@")) {
            return "Email inválido.";
        }

        // Crear y agregar nuevo usuario
        User newUser = new User(users.size() + 1, name, email, password, "cliente");
        users.add(newUser);
        return "Usuario registrado exitosamente.";
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
    public String updateUser(int userId, String name, String email, String password) throws Exception {
        User user = getUserById(userId);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return "Usuario actualizado exitosamente.";
    }

    // Eliminar Usuario (cambiar estado a inactivo)
    public String deactivateUser(int userId) throws Exception {
        User user = getUserById(userId);
        user.deactivate();
        return "Usuario desactivado.";
    }
    // Método para buscar un usuario por su correo electrónico
    public User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null); // Si no se encuentra el usuario, devuelve null
    }

    // ===================== Métodos para Recuperación de Cuenta =====================

    // Método para solicitar recuperación de cuenta
    public String solicitarRecuperacionCuenta(String email) {
        // Buscar al usuario por su email
        User user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        // Si no se encuentra el usuario
        if (user == null) {
            return "Usuario no encontrado.";
        }

        // Generar un código de recuperación único usando UUID
        String codigoRecuperacion = UUID.randomUUID().toString();
        user.setCode(codigoRecuperacion); // Guardar el código de recuperación en el usuario
        user.setCodeGeneratedAt(new Date()); // Guardar la fecha de generación del código

        // Simulación del envío de correo (aquí deberías implementar el servicio de correo real)
        enviarCorreoRecuperacion(user.getEmail(), codigoRecuperacion);

        return "Correo de recuperación enviado.";
    }

    // Método para actualizar la contraseña usando el código de recuperación
    public String actualizarConCodigoRecuperacion(String email, String codigoRecuperacion, String nuevaPassword, String confirmarPassword) {
        // Buscar al usuario por su email
        User user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        // Si el usuario no es encontrado
        if (user == null) {
            return "Usuario no encontrado.";
        }

        // Validar que el código de recuperación coincida
        if (!codigoRecuperacion.equals(user.getCode())) {
            return "Código de recuperación inválido.";
        }

        // Validar que el código de recuperación no haya expirado (ejemplo: 24 horas de validez)
        long horasPasadas = (new Date().getTime() - user.getCodeGeneratedAt().getTime()) / (1000 * 60 * 60);
        if (horasPasadas > 24) {
            user.setCode(null); // Invalida el código
            return "El código ha expirado.";
        }

        // Validar que las contraseñas coincidan
        if (!nuevaPassword.equals(confirmarPassword)) {
            return "Las contraseñas no coinciden.";
        }

        // Actualizar la contraseña
        user.setPassword(nuevaPassword);

        // Invalida el código de recuperación después de su uso
        user.setCode(null);
        user.setCodeGeneratedAt(null);

        return "Contraseña actualizada exitosamente.";
    }

    // Método simulado para enviar el correo de recuperación
    private void enviarCorreoRecuperacion(String email, String codigoRecuperacion) {
        // Aquí iría el código real para enviar un correo, pero en este ejemplo solo lo simulamos
        System.out.println("Enviando correo a " + email + " con el código de recuperación: " + codigoRecuperacion);
    }

}
