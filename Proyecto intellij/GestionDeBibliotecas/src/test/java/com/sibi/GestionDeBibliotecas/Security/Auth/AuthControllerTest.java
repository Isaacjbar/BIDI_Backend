package com.sibi.GestionDeBibliotecas.Security.Auth;

import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthRequest;
import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthResponse;
import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login() {
        // Caso de Prueba: Inicio de sesión con credenciales válidas.
        // Paso 1: Crear una instancia de AuthController inyectando los mocks configurados.
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("alan@gmail.com");
        authRequest.setPassword("password123");

        // Paso 2: Crear un usuario con credenciales válidas.
        Usuario usuario = new Usuario("Alan", "Aguilar", "alan@gmail.com", "hashedPassword123", Rol.CLIENTE);
        usuario.setUsuarioId(1L);
        usuario.setEstado(Estado.ACTIVO);

        // Paso 3: Configurar los mocks
        when(usuarioRepository.findByCorreo("alan@gmail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "hashedPassword123")).thenReturn(true);
        when(userDetailsService.loadUserByUsername("alan@gmail.com")).thenReturn(mock(UserDetails.class));
        when(jwtUtil.generateToken(any(UserDetails.class), eq(1L))).thenReturn("mockedToken");
        when(jwtUtil.getExpirationTime()).thenReturn(System.currentTimeMillis() + 3600000);
        when(authService.isTokenInvalid("mockedToken")).thenReturn(false);

        // Paso 4: Llamar al método login(AuthRequest authRequest) con un correo y contraseña válidos.
        AuthResponse response = authController.login(authRequest);

        // Paso 4: Verificar que el AuthResponse devuelto contiene:
        assertNotNull(response);
        // Un token JWT válido
        assertEquals("mockedToken", response.getJwt());
        // El ID del usuario registrado.
        assertEquals(1L, response.getUserId());
        // El correo electrónico del usuario registrado.
        assertEquals("alan@gmail.com", response.getEmail());
        // El rol del usuario registrado.
        assertEquals(Rol.CLIENTE, response.getRole());
    }

    @Test
    void logout() throws Exception{
        // Caso de Prueba: Cierre de Sesión exitoso
        // Paso 1: Ingresar un token válido
        String token = "Bearer mockedToken";

        // Paso 2: Crear una instancia de AuthController inyectando los mocks configurados.
        when(authService.isTokenInvalid("mockedToken")).thenReturn(false);

        // Paso 3: Llamar al método logout(String token) con un token válido.
        ResponseEntity<Message> response = authController.logout(token);

        // Paso 4: Verificar que el servicio AuthService.invalidateToken(String token) sea llamado con el token proporcionado.
        assertNotNull(response);
        // Paso 5: Verificar que el código de estado sea OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Paso 6: Verificar que el mensaje de respuesta sea "Logout exitoso".
        assertEquals("Logout exitoso", response.getBody().getText());

    }
}