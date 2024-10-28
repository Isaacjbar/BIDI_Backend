package mx.edu.utez.controller;

import mx.edu.utez.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mx.edu.utez.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mx.edu.utez.model.Loan;
import mx.edu.utez.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserController {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Loan> loans = new ArrayList<>();
    private LoanController loanController;

    public UserController(LoanController loanController) {
        this.loanController = loanController;
    }


    // Iniciar sesión
    public String loginUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return "Datos incorrectos.";
        }
        // Simulación de inicio de sesión exitoso
        System.out.println("Iniciando sesión de: " + email);
        return "Inicio de sesión exitoso.";
    }


    // Cerrar Sesión
    public String logoutUser(String email) {
        User user = getUserByEmail(email);
        if (user == null) {
            return "Usuario no encontrado.";
        }
        // Simulación de invalidación del token de sesión
        System.out.println("Cerrando sesión de: " + email);
        return "Sesión cerrada exitosamente.";
    }

    // Crear Usuario
    public String addUser(String name, String lastName, String email, String phone, String password, String confirmPassword) {
        // Verificar que todos los campos sean proporcionados
        if (name == null || lastName == null || email == null || phone == null || password == null || confirmPassword == null) {
            return "Todos los campos son obligatorios.";
        }

        // Validar que el correo no esté en uso
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return "Correo en uso";
            }
        }

        // Validar que el número de teléfono no esté en uso
        for (User user : users) {
            if (user.getPhone().equals(phone)) {
                return "Teléfono en uso";
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

        // Validar que el número de teléfono sea válido
        if (!phone.matches("\\d{10}")) {
            return "Teléfono no válido";
        }

        // Crear y agregar nuevo usuario
        User newUser = new User(users.size() + 1, name, lastName, email, phone, password, "cliente");
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
            if (user.getStatus().equalsIgnoreCase(status)) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }


    //Actualizar Usuario
    public String updateUser(int userId, String name, String lastName, String email, String password) throws Exception {
        User user = getUserById(userId);

        if (user == null) {
            return "Usuario no encontrado.";
        }

        // Validar que el nombre no sea nulo o vacío
        if (name == null || name.isEmpty()) {
            return "Nombre no puede estar vacío";
        }

        // Validar que el apellido no sea nulo
        if (lastName == null || lastName.isEmpty()) {
            return "Apellido no puede estar vacío";
        }

        if (email == null || email.isEmpty()) {
            return "Email no puede estar vacío";
        } else if (!email.contains("@")) {
            return "Email inválido";
        }

        if (password == null || password.isEmpty()) {
            return "Contraseña no puede estar vacía";
        }

        // Verificar si el correo ya está en uso por otro usuario
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getUserId() != userId) {
                return "Correo en uso";
            }
        }

        // Actualizar los valores del usuario
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return "Usuario actualizado exitosamente";
    }

    private boolean hasPendingLoansForUser(int userId) {
        return loans.stream()
                .anyMatch(loan -> loan.getUser().getUserId() == userId && !loan.isReturned());
    }

    // Método para desactivar un usuario
    public String desactivateUser(int userId) throws Exception {
        User user = getUserById(userId);
        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }

        // Verificar si el usuario tiene préstamos pendientes
        if (hasPendingLoansForUser(userId)) {
            return "No se puede desactivar un usuario con préstamos pendientes.";
        }

        // Desactivar el usuario
        user.setStatus("inactive");
        return "Usuario desactivado.";
    }


    // Método para buscar un usuario por su correo electrónico
    public User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null); // Si no se encuentra el usuario, devuelve null
    }

    // Consultar perfil del usuario
    public User getProfile(int userId) throws Exception {
        return getUserById(userId);
    }

    // Método para actualizar el perfil del usuario
    public String updateProfileUser(int userId, String name, String lastName, String email, String phone, String password, String repeatPassword) {
        User user;
        try {
            user = getUserById(userId);
        } catch (Exception e) {
            return "Usuario no encontrado.";
        }

        // Validar que todos los campos sean proporcionados
        if (name == null || lastName == null || email == null || phone == null || password == null || repeatPassword == null) {
            return "Todos los campos son obligatorios.";
        }

        // Validar que el correo no esté en uso por otro usuario
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getUserId() != userId) {
                return "Correo en uso";
            }
        }

        // Validar que el número de teléfono no esté en uso por otro usuario
        for (User u : users) {
            if (u.getPhone().equals(phone) && u.getUserId() != userId) {
                return "Teléfono en uso";
            }
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(repeatPassword)) {
            return "Contraseñas diferentes";
        }

        // Validar formato de correo
        if (!email.contains("@")) {
            return "Email inválido.";
        }

        // Validar que el número de teléfono sea válido
        if (!phone.matches("\\d{10}")) {
            return "Teléfono no válido";
        }

        // Actualizar los datos del usuario
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        return "Perfil actualizado exitosamente.";
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
