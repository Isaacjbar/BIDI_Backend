package com.sibi.GestionDeBibliotecas.Util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:8080/sibi") // Cambia a tu dominio real
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                .allowedHeaders("Authorization", "Content-Type", "Accept") // Encabezados permitidos
                .allowCredentials(true); // Permite el uso de cookies y credenciales
    }
}