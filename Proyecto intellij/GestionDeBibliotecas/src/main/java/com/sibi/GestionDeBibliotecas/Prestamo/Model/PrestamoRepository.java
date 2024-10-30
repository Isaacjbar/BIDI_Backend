package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

}
