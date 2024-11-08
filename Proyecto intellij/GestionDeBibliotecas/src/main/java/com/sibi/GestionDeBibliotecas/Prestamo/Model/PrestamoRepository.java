package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    List<Prestamo> findByStatus(Prestamo.Status status);
    List<Prestamo> findByUsuario(Usuario usuario);
}
