package com.sibi.GestionDeBibliotecas.Util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas las rutas
                .allowedOrigins("*") // Permite desde todos los origenes
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite los métodos HTTP especificados
                .allowedHeaders("*")// Permite todos los headers
                .allowCredentials(true); // Permite enviar cookies y autenticación
//        .allowedHeaders("Authorization", "Content-Type")// Permite todos los headers
//                .allowCredentials(true); // Permite enviar cookies y autenticación
    }
}