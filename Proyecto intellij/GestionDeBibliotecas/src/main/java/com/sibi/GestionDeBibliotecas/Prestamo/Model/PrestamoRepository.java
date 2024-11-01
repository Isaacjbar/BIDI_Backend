package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    List<Prestamo> findByStatus(Prestamo.Status status);
}
