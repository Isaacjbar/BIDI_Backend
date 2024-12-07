package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sibi.GestionDeBibliotecas.Util.Services.EmailService;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        // Caso de Prueba: Visualización de todos los usuarios registrados (Administrador)

        // Paso 1: Crear usuarios simulados.
        Usuario usuario1 = new Usuario("Alan", "Aguilar", "alan@gmail.com", "password123", Rol.CLIENTE);
        Usuario usuario2 = new Usuario("Isaac", "Barcelata", "isaac@gmail.com", "password123", Rol.CLIENTE);
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        // Paso 2: Configurar el mock para simular la consulta de todos los usuarios.
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Paso 3: Llamar al método findAll() para obtener la lista de usuarios registrados.
        ResponseEntity<Message> response = usuarioService.findAll();

        // Paso 4: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Listado de usuarios", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }

    @Test
    void findById() {
        // Caso de Prueba: Consulta de usuarios por ID

        // Paso 1: Crear un usuario de prueba con un ID específico.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "password123", Rol.CLIENTE);
        usuario.setUsuarioId(1L);

        // Paso 2: Configurar el mock para simular la búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Paso 3: Crear un objeto UsuarioDTO con el ID del usuario.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuarioId(1L);

        // Paso 4: Llamar al método findById() con el objeto UsuarioDTO.
        ResponseEntity<Message> response = usuarioService.findById(usuarioDTO);

        // Paso 5: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Paso 6: Verificar el mensaje de éxito.
        assertEquals("Usuario", response.getBody().getText());
        assertEquals(usuario, response.getBody().getResult());
    }

    @Test
    void find() {
        // Caso de Prueba: Consulta de perfil exitoso

        // Paso 1: Crear un usuario de prueba con un ID específico.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "password123", Rol.CLIENTE, "7774419806");
        usuario.setUsuarioId(1L);

        // Paso 2: Crear un objeto UsuarioDTO con el ID del usuario.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuarioId(1L);

        // Paso 3: Simular un token válido para el usuario.
        String token = "Bearer validToken";
        when(jwtUtil.extractUserId("validToken")).thenReturn(1L);

        // Paso 4: Configurar el mock del repositorio para simular la recuperación del usuario.
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Paso 5: Llamar al método find() con el objeto UsuarioDTO y el token.
        ResponseEntity<Message> response = usuarioService.find(usuarioDTO, token);

        // Paso 6: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario", response.getBody().getText());
        assertEquals(usuario, response.getBody().getResult());

    }

    @Test
    void save() {
        // Caso de Prueba: Registro de usuario exitoso
        // Paso 1: Crear un objeto UsuarioDTO con datos válidos.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Alan");
        usuarioDTO.setApellidos("Aguilar");
        usuarioDTO.setCorreo("alan@gmail.com");
        usuarioDTO.setContrasena("Password123");
        usuarioDTO.setNumeroTelefono("7774419806");

        // Paso 2: Crear una instancia de UsuarioService inyectando los mocks configurados para simular el comportamiento del repositorio.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "hashedPassword", Rol.CLIENTE, "7771234567");
        when(usuarioRepository.findUsuarioByCorreo("alan@gmail.com")).thenReturn(null);
        when(userDetailsServiceImpl.encodePassword("Password123")).thenReturn("hashedPassword");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        // Paso 3: Llamar al método save con los datos del nuevo usuario.
        ResponseEntity<Message> response =  usuarioService.save(usuarioDTO);

        // Paso 4: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El usuario se registró correctamente", response.getBody().getText());
        assertEquals(usuario, response.getBody().getResult());
    }

    @Test
    void modify() {
        // Caso de Prueba: Modificación exitosa de datos de usuario

        // Paso 1: Crear un usuario de prueba con datos iniciales.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "Password123", Rol.CLIENTE, "7774419806");
        usuario.setUsuarioId(1L);

        // Paso 2: Crear un objeto UsuarioDTO con los nuevos datos a modificar.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuarioId(1L);
        usuarioDTO.setNombre("Alan");
        usuarioDTO.setApellidos("Aguilar");
        usuarioDTO.setCorreo("alan@gmail.com");
        usuarioDTO.setNumeroTelefono("7771234567");
        usuarioDTO.setContrasena("Password123");

        // Paso 3: Configurar los mocks para simular el comportamiento del sistema.
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(userDetailsServiceImpl.encodePassword("Password123")).thenReturn("hashedPassword");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        // Paso 4: Llamar al método modify() con el objeto UsuarioDTO.
        ResponseEntity<Message> response = usuarioService.modify(usuarioDTO);

        // Paso 5: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Paso 6: Verificar el mensaje de éxito.
        assertEquals("El usuario se actualizó correctamente", response.getBody().getText());
        assertEquals(usuario, response.getBody().getResult());

    }

    @Test
    void updateForCustomer() {
        // Caso de Prueba: Edición de perfil exitoso

        // Paso 1: Crear un usuario de prueba con datos originales.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "Password123", Rol.CLIENTE, "7774419806");
        usuario.setUsuarioId(1L);

        // Paso 2: Crear un objeto UsuarioDTO con los datos actualizados.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuarioId(1L);
        usuarioDTO.setNombre("Alan");
        usuarioDTO.setApellidos("Canchola");
        usuarioDTO.setCorreo("alan_aguilar@gmail.com");
        usuarioDTO.setNumeroTelefono("7771234567");
        usuarioDTO.setContrasena("Password123");

        // Paso 3: Simular un token válido para el usuario.
        String token = "Bearer validToken";
        when(jwtUtil.extractUserId("validToken")).thenReturn(1L);

        // Paso 4: Configurar los mocks para simular la recuperación y actualización del usuario.
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(userDetailsServiceImpl.encodePassword("Password123")).thenReturn("hashedNewPassword");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Paso 5: Llamar al método updateForCustomer() con el objeto UsuarioDTO y el token.
        ResponseEntity<Message> response = usuarioService.updateForCustomer(usuarioDTO, token);

        // Paso 6: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Exito al editar tu información", response.getBody().getText());
    }

    @Test
    void changeStatus() {
        // Caso de Prueba: Cambiar estado de usuario a inactivo sin préstamos pendientes

        // Paso 1: Crear un usuario de prueba con estado ACTIVO y sin préstamos pendientes.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "password123", Rol.CLIENTE, "7774419806");
        usuario.setUsuarioId(1L);
        usuario.setEstado(Estado.ACTIVO);

        // Paso 2: Crear un objeto UsuarioDTO con el ID del usuario.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuarioId(1L);

        // Paso 3: Configurar los mocks para simular el comportamiento del sistema.
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.countPrestamosByUsuarioId(1L)).thenReturn(0L); // Sin préstamos activos
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Paso 4: Llamar al método changeStatus() con el objeto UsuarioDTO.
        ResponseEntity<Message> response = usuarioService.changeStatus(usuarioDTO);

        // Paso 5: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Paso 6: Verificar el mensaje de éxito.
        assertEquals("El estado del usuario se actualizó correctamente", response.getBody().getText());

    }

    @Test
    void findByStatusActive() {
        // Caso de Prueba: Consultar usuarios por estado (activo)

        // Paso 1: Crear datos de prueba con usuarios activos.
        Usuario usuario1 = new Usuario("Alan", "Aguilar", "alan@gmail.com", "Password123", Rol.CLIENTE, "7771234567");
        Usuario usuario2 = new Usuario("Isaac", "Barcelata", "isaac@gmail.com", "Password321",  Rol.CLIENTE, "7774419806");
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        // Paso 2: Configurar el mock para simular la búsqueda de usuarios activos.
        when(usuarioRepository.findByEstado(Estado.ACTIVO)).thenReturn(usuarios);

        // Paso 3: Llamar al método findByStatus con el estado "ACTIVO".
        ResponseEntity<Message> response = usuarioService.findByStatus("ACTIVO");

        // Paso 4: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Paso 5: Verificar que regrese el listado de usuarios con estado activo.
        assertEquals("Listado de usuarios por estado", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }

    @Test
    void findByStatusInactive() {
        // Caso de Prueba: Consultar usuarios por estado (inactivo)

        // Paso 1: Crear datos de prueba con usuarios inactivos.
        Usuario usuario1 = new Usuario("Alan", "Aguilar", "alan@gmail.com", "Password123", Rol.CLIENTE, "7771234567");
        Usuario usuario2 = new Usuario("Isaac", "Barcelata", "isaac@gmail.com", "Password321",  Rol.CLIENTE, "7774419806");
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        // Paso 2: Configurar el mock para simular la búsqueda de usuarios inactivos.
        when(usuarioRepository.findByEstado(Estado.INACTIVO)).thenReturn(usuarios);

        // Paso 3: Llamar al método findByStatus con el estado "INACTIVO".
        ResponseEntity<Message> response = usuarioService.findByStatus("INACTIVO");

        // Paso 4: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Paso 5: Verificar que regrese el listado de usuarios con estado inactivo.
        assertEquals("Listado de usuarios por estado", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }

    @Test
    void requestPasswordReset() {
        // Caso de Prueba: Solicitud de recuperación de cuenta exitosa

        // Paso 1: Crear un usuario de prueba con estado ACTIVO.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "password123", Rol.CLIENTE, "7774419806");
        usuario.setEstado(Estado.ACTIVO);

        // Paso 2: Crear un objeto UsuarioDTO con el correo del usuario.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCorreo("alan@gmail.com");

        // Paso 3: Configurar los mocks.
        when(usuarioRepository.findByCorreo("alan@gmail.com")).thenReturn(Optional.of(usuario));
        doNothing().when(emailService).sendEmail(
                eq("john.doe@example.com"),
                eq("Recuperación de contraseña BIDI"),
                anyString()
        );

        // Paso 4: Llamar al método requestPasswordReset() con el objeto UsuarioDTO.
        ResponseEntity<Message> response = usuarioService.requestPasswordReset(usuarioDTO);

        // Paso 5: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Correo de recuperación enviado correctamente, verifica tu bandeja de entrada", response.getBody().getText());

    }

    @Test
    void resetPassword() {
        // Caso de Prueba: Actualización exitosa de contraseña con código válido

        // Paso 1: Crear un usuario de prueba con un código de recuperación activo.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "Password123", Rol.CLIENTE, "7774419806");
        usuario.setCodigo("Cod123");
        usuario.setCodigoGeneradoEn(LocalDateTime.now().minusMinutes(10));

        // Paso 2: Crear un objeto UsuarioDTO con el correo, el código y la nueva contraseña.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCorreo("alan@gmail.com");
        usuarioDTO.setCodigo("Cod123");
        usuarioDTO.setContrasena("newPassword123");

        // Paso 3: Configurar los mocks.
        when(usuarioRepository.findByCodigoAndCorreo("Cod123", "alan@gmail.com")).thenReturn(Optional.of(usuario));
        when(userDetailsServiceImpl.encodePassword("newPassword123")).thenReturn("hashedNewPassword");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        // Paso 4: Llamar al método resetPassword() con el objeto UsuarioDTO.
        ResponseEntity<Message> response = usuarioService.resetPassword(usuarioDTO);

        // Paso 5: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contraseña restablecida correctamente, inicia sesión.", response.getBody().getText());

        // Paso 6: Validar que la contraseña fue actualizada correctamente.
        assertEquals("hashedNewPassword", usuario.getContrasena());
        assertEquals("", usuario.getCodigo());
        assertNull(usuario.getCodigoGeneradoEn());
    }

    @Test
    void resetPasswordProfile() {
        // Caso de Prueba: Actualización de contraseña de perfil exitosa

        // Paso 1: Crear un usuario de prueba con un ID y una contraseña inicia
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "Password123", Rol.CLIENTE, "7774419806");
        usuario.setUsuarioId(1L);

        // Paso 2: Crear un objeto UsuarioDTO con el ID y la nueva contraseña.
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsuarioId(1L);
        usuarioDTO.setContrasena("newPassword");

        // Paso 3: Configurar los mocks para simular la validación del token y la recuperación del usuario.
        when(jwtUtil.extractUserId("validToken")).thenReturn(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Paso 4: Configurar el mock del servicio de encriptación para la nueva contraseña.
        when(userDetailsServiceImpl.encodePassword("newPassword")).thenReturn("hashedNewPassword");

        // Paso 5: Llamar al método resetPasswordProfile() con el objeto UsuarioDTO y el token.
        ResponseEntity<Message> response = usuarioService.resetPasswordProfile(usuarioDTO, "Bearer validToken");

        // Paso 6: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contraseña actualizada correctamente", response.getBody().getText());

        // Paso 7: Validar que la contraseña fue actualizada correctamente.
        assertEquals("hashedNewPassword", usuario.getContrasena());

    }
}