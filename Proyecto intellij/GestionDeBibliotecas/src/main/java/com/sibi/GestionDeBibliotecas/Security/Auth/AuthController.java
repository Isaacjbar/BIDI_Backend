package com.sibi.GestionDeBibliotecas.Security.Auth;

import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthRequest;
import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthResponse;
import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository userRepository;
    private final AuthService authService;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, UsuarioRepository userRepository, AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws Exception {
        // Busca al usuario por correo
        Usuario user = userRepository.findByCorreo(authRequest.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Verifica si la contraseña ingresada coincide con la almacenada (hashed)
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getContrasena())) {
            throw new Exception("Correo o contraseña incorrectos");
        }

        // Genera el JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Aquí podrías verificar si el token es inválido
        if (authService.isTokenInvalid(jwt)) {
            throw new Exception("Token inválido");
        }

        long expirationTime = jwtUtil.getExpirationTime();

        return new AuthResponse(jwt, user.getUsuarioId(), user.getCorreo(), expirationTime);
    }

    @PostMapping("/logout")
    public ResponseEntity<Message> logout(@RequestHeader("Authorization") String token) {
        // Extrae el token sin el prefijo "Bearer "
        String jwt = token.substring(7); // Asumiendo que el token se pasa con el prefijo "Bearer"
        authService.invalidateToken(jwt);
        Message responseMessage = new Message("Logout exitoso", TypesResponse.SUCCESS);

        //logger.info("El usuario ha cerrado sesión exitosamente");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}