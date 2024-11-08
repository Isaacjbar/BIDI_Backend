package com.sibi.GestionDeBibliotecas.Security;

import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtRequestFilter;
import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("auth/login", "user/register", "user/request-password-reset","/user/validate-token").permitAll() // Permitir acceso sin autenticación
                        .requestMatchers("/user/update", "/loan/save/for-customer", "/book/for-customer").hasRole(Rol.CLIENTE.name())
                        .requestMatchers("user/reset-password").hasAnyRole(Rol.CLIENTE.name(), Rol.ADMINISTRADOR.name())
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin estado
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Añadir filtro JWT
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}