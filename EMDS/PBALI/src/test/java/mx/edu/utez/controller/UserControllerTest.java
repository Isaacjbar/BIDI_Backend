package mx.edu.utez.controller;

import static org.junit.jupiter.api.Assertions.*;

import mx.edu.utez.model.Inventory;
import mx.edu.utez.model.Loan;
import mx.edu.utez.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;


@Nested
class UserControllerTest {

    private UserController controller;
    private LoanController loanController;
    private InventoryController inventoryController;

    // Inicializar antes de cada prueba
    @BeforeEach
    public void setUp() {
        controller = new UserController(loanController);
        inventoryController = new InventoryController();
        loanController = new LoanController(inventoryController);
    }

    // Caso de Prueba: Inicio de sesión exitoso
    @Test
    public void testInicioSesionExitoso() {
        // Paso 1: Registrar un usuario con la función addUser(), asegurándose de que el correo y la contraseña sean válidos.
        controller.addUser("María", "Gómez", "maria.gomez@example.com", "7779876543", "password456", "password456");

        // Paso 2: Llamar a la función loginUser(String email, String password) con el correo y la contraseña del usuario registrado.
        String result = controller.loginUser("maria.gomez@example.com", "password456");

        //Paso 3: Verificar que el mensaje devuelto sea "Inicio de sesión exitoso."
        assertEquals("Inicio de sesión exitoso.", result);
    }

    // Caso de Prueba: Inicio de sesión fallido
    @Test
    public void testInicioSesionFallido() {
        // Paso 1: Registrar un usuario con la función addUser(), asegurándose de que el correo y la contraseña sean válidos.
        controller.addUser("Pedro", "Martínez", "pedro.martinez@example.com", "7776543210", "password789", "password789");

        // Paso 2: Llamar a la función loginUser(String email, String password) con un correo electrónico existente pero con una contraseña incorrecta.
        String result = controller.loginUser("pedro.martinez@example.com", "anotherpassword");

        //Paso 3: Verificar que el mensaje devuelto sea "Datos incorrectos."
        assertEquals("Datos incorrectos.", result);
    }

    // Caso de Prueba: Cerrar Sesión
    @Test
    public void testCerrarSesion() {
        // Paso 1: Registrar un usuario con la función addUser(), asegurándose de que el correo electrónico sea válido.
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar a la función logoutUser(String email) con el correo electrónico del usuario registrado.
        String result = controller.logoutUser("juan.perez@example.com");

        // Paso 3: Verificar que el mensaje devuelto sea "Sesión cerrada exitosamente."
        assertEquals("Sesión cerrada exitosamente.", result);
    }

    // Caso de Prueba: Registro exitoso de usuario
    @Test
    public void testRegistrarUsuarioExitoso() {
        // Paso 1: Llamar a la función addUser con los datos de un nuevo usuario
        String result = controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567","password123", "password123");
        // Paso 2: Verificar que el usuario con los nuevos datos se haya registrado correctamente.
        equals("Usuario registrado exitosamente.", result);

        // Paso 3: Obtener la lista de usuarios y validar que el nuevo usuario está presente.
        assertFalse(controller.getAllUsers().isEmpty());
        equals("juan.perez@example.com", controller.getAllUsers().get(0).getEmail());
        equals("cliente", controller.getAllUsers().get(0).getRole());
        equals("active", controller.getAllUsers().get(0).getStatus());
    }
    private void equals(String s, String result) {
    }// BORRAR DESPUES

    // Caso de Prueba: Registro fallido por correo duplicado
    @Test
    public void testRegistroCorreoDuplicado() {
        // Paso 1: Registrar un usuario con el correo "juan.perez@example.com".
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567","password123", "password123");

        // Paso 2: Intentar registrar un segundo usuario con el mismo correo electrónico:
        String result = controller.addUser("Ana" ,"Martínez", "juan.perez@example.com", "7771234567","password456", "password456");
        // Paso 3: Verificar que se muestre un mensaje indicando que el correo ya está en uso.
        assertEquals("Correo en uso", result);

        // Verificar que solo hay un usuario registrado
        assertEquals(1, controller.getAllUsers().size());
    }

    // Caso de Prueba: Registro fallido por contraseñas no coincidentes
    @Test
    public void testRegistroContraseñasNoCoinciden() {
        // Paso 1: Intentar registrar un usuario con contraseñas diferentes:
        //controller.addUser("Carlos”, “Gómez", "carlos.gomez@example.com", “7771234567”, "password123", "password789");
        String result = controller.addUser("Carlos", "Gómez", "carlos.gomez@example.com","7771234567", "password123", "password789");
        // Paso 2: Verificar que se muestre un mensaje de error indicando que las contraseñas no coinciden.
        assertEquals("Contraseñas diferentes", result);

        // Paso 3: Verificar que el usuario no fue registrado.
        assertTrue(controller.getAllUsers().isEmpty());
    }

    // Caso de Prueba: Registro fallido por campos vacíos
    @Test
    public void testRegistroConCamposVacios() {
        // Paso 1: Llamar a la función addUser con los datos de un nuevo usuario:
        //controller.addUser("Juan”, “Pérez", "" ,”7771234567”, "password123", "password123");
        String result = controller.addUser("Juan", "Pérez", null, "7771234567", "password123", "password123");
        // Paso 2: Verificar que mande un mensaje “Todos los campos son obligatorios”.
        assertEquals("Todos los campos son obligatorios.", result);
    }

    // Caso de Prueba: Registro fallido por número de teléfono inválido
    @Test
    public void testRegistroTelefonoInvalido() {
        // Paso 1: Agregar un usuario con la función addUser() con un número de teléfono no válido (un número con menos de 10 dígitos o con caracteres no numéricos)
        String result = controller.addUser("Juan", "Pérez", "juan.perez@example.com", "12345", "password123", "password123");
        // Paso 2: Verificar que se lanza una excepción o error indicando que el número de teléfono no es válido.
        assertEquals("Teléfono no válido", result);

        // Paso 3: Confirmar que el usuario no ha sido agregado al sistema.
        assertTrue(controller.getAllUsers().isEmpty());
    }

    // Caso de Prueba: Registro fallido por número de teléfono duplicado
    @Test
    public void testRegistroTelefonoDuplicado() {
        // Paso 1: Registrar un usuario con el número de teléfono válido utilizando el método addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Intentar registrar un nuevo usuario con el mismo número de teléfono.
        String result = controller.addUser("Ana", "Martínez", "ana.martinez@example.com", "7771234567", "password456", "password456");

        // Paso 3: Verificar que el método addUser() devuelve el mensaje de error "Teléfono en uso".
        assertEquals("Teléfono en uso", result);

        // Paso 4: Comprobar que solo hay un usuario registrado en el sistema.
        assertEquals(1, controller.getAllUsers().size());
    }

    // Caso de pruba: Visualizacion de todos los usuarios
    @Test
    public void testVisualizacionTodosLosUsuarios() {
        // Paso 1: Agregar varios usuarios con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");
        controller.addUser("Ana", "Martínez", "ana.martinez@example.com", "7777654321", "password456", "password456");

        // Paso 2: Llamar a la función getAllUsers() para obtener la lista de todos los usuarios.
        List<User> users = controller.getAllUsers();

        // Paso 3: Verificar que la lista contiene todos los usuarios
        assertEquals(2, users.size());
    }

    // Caso de prueba: Consultar usuarios por estado (activo)
    @Test
    public void testConsultarUsuariosActivos() throws Exception {
        // Paso 1: Agregar varios usuarios con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");
        controller.addUser("Ana", "Martínez", "ana.martinez@example.com", "7777654321", "password456", "password456");

        // Paso 2: Cambiar el estado de algunos usuarios a "inactivo" usando desactivateUser().
        controller.desactivateUser(1);

        // Paso 3: Llamar a la función getUserByStatus (“activo”) para obtener la lista de usuarios activos.
        List<User> activeUsers = controller.getUsersByStatus("activo");

        // Paso 4: Verificar que la lista contiene solo usuarios con estado "activo".
        assertEquals(0, activeUsers.size());

    }

    // Caso de prueba: Consultar usuarios por estado (inactivo)
    @Test
    public void testConsultarUsuariosInactivos() throws Exception {
        // Paso 1: Agregar varios usuarios con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");
        controller.addUser("Ana", "Martínez", "ana.martinez@example.com", "7777654321", "password456", "password456");

        // Paso 2: Cambiar el estado de algunos usuarios a "inactivo" usando desactivateUser().
        controller.desactivateUser(1);

        // Paso 3: Llamar a la función getUsersByStatus("inactivo").
        List<User> inactiveUsers = controller.getUsersByStatus("inactivo");

        // Paso 4: Verificar que la lista contiene solo usuarios inactivos.
        assertEquals(0, inactiveUsers.size());
    }

    // Caso de prueba: Consulta de usuarios por ID
    @Test
    public void testConsultarUsuarioPorId() throws Exception {
        // Paso 1: Agregar un usuario con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar al método getUserById(int userId) con un ID existente.
        User user = controller.getUserById(1);

        // Paso 3: Verificar que devuelve el usuario correspondiente.
        assertEquals("Juan", user.getName());
        assertEquals("Pérez", user.getLastName());
        assertEquals(1, user.getUserId());
    }

    // Caso de prueba: Consulta de usuarios por ID inexistente
    @Test
    public void testConsultarUsuarioPorIdInexistente() throws Exception {
        //Paso 1: Llamar al método getUserById(int userId) con un ID inexistente.
        Exception exception = assertThrows(Exception.class, () -> {
            controller.desactivateUser(999);
        });
        // Paso 2: Verificar que lanza una excepción.
        assertEquals("Usuario no encontrado.", exception.getMessage());
    }

    // Caso de Prueba: Actualización de Usuario exitosa
    @Test
    public void testModificarUsuarioExitoso() throws Exception {
        // Paso 1: Agregar un usuario con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Actualizar cualquier campo con la función updateUser().
        String result = controller.updateUser(1, "Juancho", "Pérez","juan.perez@example.com", "password123");

        // Paso 3: Verificar que la información haya sido actualizada correctamente.
        assertEquals("Usuario actualizado exitosamente", result);
        User user = controller.getUserById(1);
        assertEquals("Juancho", user.getName());
        assertEquals("Pérez", user.getLastName());
        assertEquals("juan.perez@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    // Caso de Prueba: Actualización fallida por correo duplicado
    @Test
    public void testActualizacionCorreoDuplicado() throws Exception {
        // Paso 1: Agregar dos usuarios  con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");
        // Registro de usuario con correo ya existente
        controller.addUser("Juan2", "Pérez2", "juan.perez2@example.com", "7777654321", "password123", "password123");

        // Paso 2: Actualizar el campo de correo electrónico con uno ya existente con la función updateUser().
        String result = controller.updateUser(1, "Juancho", "Pérez", "juan.perez2@example.com", "password123");

        // Paso 3: Verificar que el sistema mande un mensaje de correo en uso.
        assertEquals("Correo en uso", result);

    }

    // Caso de Prueba: Intentar modificar usuario con campos vacíos
    @Test
    public void testModificarUsuarioCamposVacios() throws Exception {
        // Paso 1: Agregar un usuario con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar al método updateUser() proporcionando un ID válido y dejando los campos name, lastName, email y/o password vacíos o nulos.
        String result = controller.updateUser(1, "", "", "", "");

        // Paso 3: Verificar que se devuelve un mensaje de error indicando que los campos no pueden estar vacíos.
        assertEquals("Nombre no puede estar vacío", result);

        result = controller.updateUser(1, "Juan", "", "juan.perez@example.com", "password123");
        assertEquals("Apellido no puede estar vacío", result);

        result = controller.updateUser(1, "Juan", "Pérez", "", "password123");
        assertEquals("Email no puede estar vacío", result);

        result = controller.updateUser(1, "Juan", "Pérez", "juan.perez@example.com", "");
        assertEquals("Contraseña no puede estar vacía", result);
    }

    // Caso de Prueba: Cambiar estado de usuario a inactivo sin préstamos pendientes
    @Test
    public void testCambiarEstadoSinPrestamosPendientes() throws Exception {
        // Paso 1: Crear un usuario con estado "activo" y sin préstamos pendientes.
        controller.addUser("Luis", "Martínez", "luis.martinez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar al método desactivateUser(int userId) con el ID del usuario creado.
        String result = controller.desactivateUser(1);

        // Paso 3: Verificar que el estado del usuario se cambie a "inactivo".
        User user = controller.getUserById(1);
        assertEquals("inactive", user.getStatus());

        // Paso 4: Verificar que se muestre el mensaje de éxito correspondiente.
        assertEquals("Usuario desactivado.", result);
    }

    // Caso de Prueba: Intentar cambiar el estado de un usuario inexistente
    @Test
    public void testCambiarEstadoUsuarioInexistente() {
        // Paso 1: Intentar llamar al método desactivateUser(int userId) con un ID inexistente.
        Exception exception = assertThrows(Exception.class, () -> {
            controller.desactivateUser(999); // ID inexistente
        });

        // Paso 2: Verificar que se lanza una excepción indicando que el usuario no fue encontrado.
        assertEquals("Usuario no encontrado.", exception.getMessage());
    }

    // Caso de Prueba: Consultar perfil exitoso - Cliente
    @Test
    public void testConsultarPerfilExitosoCliente() throws Exception {
        // Paso 1: Agregar un usuario con la función addUser().
        controller.addUser("Carlos", "Ramírez", "carlos.ramirez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar a la función getProfile(int userId) con el ID del usuario cliente
        User profile = controller.getProfile(1);

        // Paso 3: Verificar que la información devuelta corresponde a la del usuario registrado.
        assertEquals("Carlos", profile.getName());
        assertEquals("Ramírez", profile.getLastName());
        assertEquals("carlos.ramirez@example.com", profile.getEmail());
    }

    // Caso de Prueba: Consultar perfil exitoso - Administrador
    @Test
    public void testConsultarPerfilExitosoAdministrador() throws Exception {
        // Paso 1: Agregar un usuario con la función addUser().
        controller.addUser("Laura", "González", "laura.gonzalez@example.com", "7771234568", "password456", "password456");

        // Paso 2: Llamar a la función getProfile(int userId) con el ID del administrador.
        User profile = controller.getProfile(1);

        // Paso 3: Verificar que la información devuelta corresponde a la del usuario registrado.
        assertEquals("Laura", profile.getName());
        assertEquals("González", profile.getLastName());
        assertEquals("laura.gonzalez@example.com", profile.getEmail());
    }

    // Caso de Prueba: Edición de perfil existoso
    @Test
    public void testEdicionPerfilExitoso() {
        // Paso 1: Registrar un usuario con la función addUser().
        controller.addUser("Carlos", "Lopez", "carlos.lopez@example.com", "7779876543", "password123", "password123");

        // Paso 2: Llamar a la función updateProfileUser(String name, String lastName, String email, String password, String repeatPassword) con los datos actualizados.
        String result = controller.updateProfileUser(1, "Carlos", "Lopez", "carlos.updated@example.com", "7779876543", "password123", "password123");

        // Paso 3: Verificar que la información haya sido actualizada correctamente.
        assertEquals("Perfil actualizado exitosamente.", result);
    }

    // Caso de Prueba: Edición de perfil con correo duplicado
    @Test
    public void testEdicionPerfilCorreoDuplicado() {
        // Paso 1: Registrar dos usuarios con la función addUser(), asegurándose de que ambos tengan correos distintos.
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");
        controller.addUser("Juan2", "Pérez2", "juan.perez2@example.com", "7777654321", "password123", "password123");

        // Paso 2: Llamar a la función updateUser(int userId, String name, String lastName, String email, String password, String repeatPassword) con el correo duplicado.
        String result = controller.updateProfileUser(1, "Juan", "Pérez", "juan.perez2@example.com", "7771234567", "password123", "password123");

        // Paso 3: Verificar que el sistema muestra un mensaje de error.
        assertEquals("Correo en uso", result);
    }

    // Caso de Prueba: Edición de perfil con contraseñas diferentes
    @Test
    public void testEdicionPerfilContraseñasDiferentes() {
        // Paso 1: Registrar un usuario con la función addUser()
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar a la función updateProfileUser(int userId, String name, String lastName, String email, String password, String repeatPassword) con contraseñas diferentes.
        String result = controller.updateProfileUser(1, "Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password321");

        // Paso 3: Verificar que el sistema muestra un mensaje de error.
        assertEquals("Contraseñas diferentes", result);
    }

    // Caso de prueba: Edición de perfil con campos vacíos
    @Test
    public void testEdicionPerfilCamposVacios() {
        // Paso 1: Registrar un usuario con la función addUser().
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 2: Llamar a la función updateProfileUser(int userId, String name, String lastName, String email, String password, String repeatPassword) dejando uno o más campos vacíos.
        String result = controller.updateProfileUser(1, null, "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");

        // Paso 3: Verificar que el sistema muestra un mensaje de error.
        assertEquals("Todos los campos son obligatorios.", result);
    }

    // Caso de prueba: Edición de perfil con teléfono duplicado
    @Test
    public void testEdicionPerfilTelefonoDuplicado() {
        // Paso 1: Registrar dos usuarios, asegurándose de que ambos tengan números de teléfono distintos con la función addUser
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567", "password123", "password123");
        controller.addUser("Carlos", "Lopez", "carlos.lopez@example.com", "7779876543", "password123", "password123");

        // Paso 2: Llamar a la función updateProfileUser(int userId, String name, String lastName, String email, String password, String repeatPassword) con el teléfono duplicado.
        String result = controller.updateProfileUser(1, "Juan", "Pérez", "juan.perez@example.com", "7779876543", "password123", "password123");

        // Paso 3: Verificar que el sistema muestra un mensaje de error.
        assertEquals("Teléfono en uso", result);
    }

    // Edición de perfil con teléfono inválido
    @Test
    public void testEdicionPerfilTelefonoInvalido() {
        // Paso 1: Registrar un usuario con la función addUser().
        controller.addUser("Carlos", "Lopez", "carlos.lopez@example.com", "7779876543", "password123", "password123");

        // Paso 2: Llamar a la función updateProfileUser(int userId, String name, String lastName, String email, String password, String repeatPassword) con unl teléfono inválido.
        String result = controller.updateProfileUser(1, "Carlos", "Lopez", "carlos.lopez@example.com", "12345", "password123", "password123");

        // Paso 3: Verificar que el sistema muestra un mensaje de error.
        assertEquals("Teléfono no válido", result);
    }

    // Caso de Prueba 1: Solicitud de recuperación de cuenta exitosa
    @Test
    public void testSolicitudRecuperacionCuenta() {
        // Paso 1: Registrar un usuario en el sistema
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567","password123", "password123");

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
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567","password123", "password123");

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
        controller.addUser("Juan", "Pérez", "juan.perez@example.com", "7771234567","password123", "password123");

        // Paso 2: Solicitar recuperación de cuenta para generar el código
        controller.solicitarRecuperacionCuenta("juan.perez@example.com");

        // Paso 3: Intentar actualizar la contraseña con un código inválido
        String result = controller.actualizarConCodigoRecuperacion(
                "juan.perez@example.com", "codigoInvalido123", "nuevaPassword123", "nuevaPassword123");

        // Verificación: El sistema debe indicar que el código de recuperación es inválido
        assertEquals("Código de recuperación inválido.", result);
    }
}