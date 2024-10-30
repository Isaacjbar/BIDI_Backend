package com.sibi.GestionDeBibliotecas.Usuario.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT usuario.correo FROM Usuario usuario where usuario.correo like  %:correo%")
    String findUsuarioByCorreo(@Param("correo") String correo);
}