package com.sibi.GestionDeBibliotecas.Usuario.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
