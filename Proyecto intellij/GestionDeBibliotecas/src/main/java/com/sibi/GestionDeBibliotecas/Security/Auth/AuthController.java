package com.sibi.GestionDeBibliotecas.Security.Auth;

import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthRequest;
import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthResponse;
import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
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
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        Optional<Usuario> userOptional = userRepository.findByCorreo(authRequest.getEmail());
        if (userOptional.isEmpty()) {
            return new AuthResponse("", 0L, "Usuario no encontrado", 0, Rol.INVITADO);
        }

        Usuario user = userOptional.get();
        if (user.getEstado() != Estado.ACTIVO) {
            return new AuthResponse("", 0L, "El usuario está inactivo", 0, Rol.INVITADO);
        }

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getContrasena())) {
            return new AuthResponse("", 0L, "Correo o contraseña incorrectos", 0, Rol.INVITADO);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails, user.getUsuarioId());

        if (authService.isTokenInvalid(jwt)) {
            return new AuthResponse("", 0L, "Sesión expirada", 0, Rol.INVITADO);
        }

        long expirationTime = jwtUtil.getExpirationTime();

        return new AuthResponse(jwt, user.getUsuarioId(), user.getCorreo(), expirationTime, user.getRol());
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(@RequestHeader(value = "Authorization", required = false) String token) throws Exception {
        if (token == null || token.trim().isEmpty()) {
            return new ResponseEntity<>(new Message("Hubo un error", TypesResponse.ERROR), HttpStatus.UNAUTHORIZED);
        }
        if (!token.startsWith("Bearer ")) {
            return new ResponseEntity<>(new Message("Hubo un error", TypesResponse.ERROR), HttpStatus.UNAUTHORIZED);
        }
        if (authService.isTokenInvalid(token)) {
            throw new Exception("Token inválido");
        }

        String jwt = token.substring(7);
        authService.invalidateToken(jwt);

        return new ResponseEntity<>(new Message("Logout exitoso", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}