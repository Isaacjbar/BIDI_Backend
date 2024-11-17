package com.sibi.GestionDeBibliotecas.Util.Config;

import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Usuario con rol de ADMINISTRADOR
            Optional<Usuario> optionalAdmin = usuarioRepository.findByCorreo("admin@example.com");
            if (!optionalAdmin.isPresent()) {
                Usuario adminUser = new Usuario(
                        "Admin User",
                        "",
                        "admin@example.com",
                        passwordEncoder.encode("admin123"),
                        Rol.ADMINISTRADOR,
                        "555-1234"
                );
                usuarioRepository.saveAndFlush(adminUser);
            }

            // Usuario con rol de CLIENTE
            Optional<Usuario> optionalCliente = usuarioRepository.findByCorreo("cliente@example.com");
            if (!optionalCliente.isPresent()) {
                Usuario clienteUser = new Usuario(
                        "Cliente User",
                        "",
                        "cliente@example.com",
                        passwordEncoder.encode("cliente123"),
                        Rol.CLIENTE,
                        "555-5678"
                );
                usuarioRepository.saveAndFlush(clienteUser);
            }

            // Usuario con rol de INVITADO
            Optional<Usuario> optionalInvitado = usuarioRepository.findByCorreo("invitado@example.com");
            if (!optionalInvitado.isPresent()) {
                Usuario invitadoUser = new Usuario(
                        "Invitado User",
                        "",
                        "invitado@example.com",
                        passwordEncoder.encode("invitado123"),
                        Rol.INVITADO,
                        "555-9876"
                );
                usuarioRepository.saveAndFlush(invitadoUser);
            }
        };
    }
}