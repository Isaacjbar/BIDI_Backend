package com.sibi.GestionDeBibliotecas.Security.Auth;

import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthRequest;
import com.sibi.GestionDeBibliotecas.Security.Auth.Dto.AuthResponse;
import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

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
        Usuario user = userRepository.findByCorreo(authRequest.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getContrasena())) {
            throw new Exception("Correo o contraseña incorrectos");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        if (authService.isTokenInvalid(jwt)) {
            throw new Exception("Token inválido");
        }

        long expirationTime = jwtUtil.getExpirationTime();

        return new AuthResponse(jwt, user.getUsuarioId(), user.getCorreo(), expirationTime);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(@RequestHeader(value = "Authorization", required = false) String token) throws Exception {
        if (authService.isTokenInvalid(token)) {
            throw new Exception("Token inválido");
        }
        String jwt = token.substring(7);
        authService.invalidateToken(jwt);

        return new ResponseEntity<>(new Message("Logout exitoso", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}