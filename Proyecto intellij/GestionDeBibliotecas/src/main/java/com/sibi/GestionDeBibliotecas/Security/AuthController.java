package com.sibi.GestionDeBibliotecas.Security;

import com.sibi.GestionDeBibliotecas.Security.Dto.AuthRequest;
import com.sibi.GestionDeBibliotecas.Security.Dto.AuthResponse;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, UsuarioRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws Exception {
        // Busca al usuario por correo
        Usuario user = userRepository.findByCorreo(authRequest.getCorreo())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Verifica si la contraseña ingresada coincide con la almacenada (hasheada)
        if (!passwordEncoder.matches(authRequest.getContrasena(), user.getContrasena())) {
            throw new Exception("Correo o contraseña incorrectos");
        }

        // Si la autenticación es exitosa, genera el JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getCorreo());
        final String jwt = jwtUtil.generateToken(userDetails);

        long expirationTime = jwtUtil.getExpirationTime();

        return new AuthResponse(jwt, user.getUsuarioId(), user.getCorreo(), expirationTime);
    }

    /* En desarrollo
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Limpiar el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // Puedes realizar cualquier acción adicional aquí si es necesario
            SecurityContextHolder.clearContext();
        }
        return new ResponseEntity<>("Logout exitoso", HttpStatus.OK);
    }*/
}