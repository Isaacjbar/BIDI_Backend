package mx.edu.utez.controller;

import static org.junit.jupiter.api.Assertions.*;

import mx.edu.utez.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserControllerTest {

    private UserController controller;

    // Inicializar antes de cada prueba
    @BeforeEach
    public void setUp() {
        controller = new UserController();
    }

    // Caso de Prueba: Registro exitoso de usuario
    @Test
    public void testRegistrarUsuarioExitoso() {
        String result = controller.addUser("Juan Pérez", "juan.perez@example.com", "password123", "password123");
        assertEquals("Usuario registrado exitosamente.", result);

        // Verificar que el usuario fue agregado a la lista
        assertFalse(controller.getAllUsers().isEmpty());
        assertEquals("juan.perez@example.com", controller.getAllUsers().get(0).getEmail());
        assertEquals("cliente", controller.getAllUsers().get(0).getRole());
        assertEquals("active", controller.getAllUsers().get(0).getStatus());
    }

    // Caso de Prueba: Registro fallido por correo duplicado
    @Test
    public void testRegistroCorreoDuplicado() {
        // Registro del primer usuario
        controller.addUser("Juan Pérez", "juan.perez@example.com", "password123", "password123");

        // Intento de registrar otro usuario con el mismo correo
        String result = controller.addUser("Ana Martínez", "juan.perez@example.com", "password456", "password456");
        assertEquals("Correo en uso", result);

        // Verificar que solo hay un usuario registrado
        assertEquals(1, controller.getAllUsers().size());
    }

    // Caso de Prueba: Registro fallido por contraseñas no coincidentes
    @Test
    public void testRegistroContraseñasNoCoinciden() {
        String result = controller.addUser("Carlos Gómez", "carlos.gomez@example.com", "password123", "password789");
        assertEquals("Contraseñas diferentes", result);

        // Verificar que no se agregó el usuario
        assertTrue(controller.getAllUsers().isEmpty());
    }
    // Caso de Prueba 1: Solicitud de recuperación de cuenta exitosa
    @Test
    public void testSolicitudRecuperacionCuenta() {
        // Paso 1: Registrar un usuario en el sistema
        controller.addUser("Juan Pérez", "juan.perez@example.com", "password123", "password123");

        // Paso 2: Solicitar recuperación de cuenta para el correo registrado
        String result = controller.solicitarRecuperacionCuenta("juan.perez@example.com");

        // Verificación: Asegurarse de que se recibe el mensaje de éxito
        assertEquals("Correo de recuperación enviado.", result);

        // Paso 3: Obtener el usuario y verificar que se ha generado un código de recuperación
        User user = controller.getUserByEmail("juan.perez@example.com");

        // Verificación: El código de recuperación no debe ser nulo
        assertNotNull(user.getCode(), "El código de recuperación debe estar generado.");
    }

    // Caso de Prueba 2: Actualización exitosa de contraseña con código válido
    @Test
    public void testActualizarConCodigoRecuperacion() {
        // Paso 1: Registrar un usuario en el sistema
        controller.addUser("Juan Pérez", "juan.perez@example.com", "password123", "password123");

        // Paso 2: Solicitar recuperación de cuenta para generar el código
        controller.solicitarRecuperacionCuenta("juan.perez@example.com");

        // Paso 3: Obtener el código de recuperación del usuario
        User user = controller.getUserByEmail("juan.perez@example.com");
        String codigoRecuperacion = user.getCode();

        // Paso 4: Actualizar la contraseña con el código de recuperación correcto
        String result = controller.actualizarConCodigoRecuperacion(
                "juan.perez@example.com", codigoRecuperacion, "nuevaPassword123", "nuevaPassword123");

        // Verificación: La actualización de la contraseña debe ser exitosa
        assertEquals("Contraseña actualizada exitosamente.", result);

        // Verificación: Asegurarse de que el código de recuperación ha sido invalidado después del uso
        assertNull(user.getCode(), "El código de recuperación debe invalidarse después de usarse.");
    }

    // Caso de Prueba 3: Fallo al actualizar contraseña por código inválido
    @Test
    public void testActualizarConCodigoRecuperacionCodigoInvalido() {
        // Paso 1: Registrar un usuario en el sistema
        controller.addUser("Juan Pérez", "juan.perez@example.com", "password123", "password123");

        // Paso 2: Solicitar recuperación de cuenta para generar el código
        controller.solicitarRecuperacionCuenta("juan.perez@example.com");

        // Paso 3: Intentar actualizar la contraseña con un código inválido
        String result = controller.actualizarConCodigoRecuperacion(
                "juan.perez@example.com", "codigoInvalido123", "nuevaPassword123", "nuevaPassword123");

        // Verificación: El sistema debe indicar que el código de recuperación es inválido
        assertEquals("Código de recuperación inválido.", result);
    }
}
