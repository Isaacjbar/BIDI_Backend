package com.sibi.GestionDeBibliotecas.Usuario.Model;

import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT usuario.correo FROM Usuario usuario where usuario.correo like  %:correo%")
    String findUsuarioByCorreo(@Param("correo") String correo);

    @Query("SELECT usuario FROM Usuario usuario WHERE usuario.usuarioId = :id")
    Optional<Usuario> findById(@Param("id") Long id);

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario.usuarioId = :usuarioId")
    Long countPrestamosByUsuarioId(@Param("usuarioId") Long usuarioId);

    List<Usuario> findByEstado(Estado estado);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCodigo(String codigo);
}