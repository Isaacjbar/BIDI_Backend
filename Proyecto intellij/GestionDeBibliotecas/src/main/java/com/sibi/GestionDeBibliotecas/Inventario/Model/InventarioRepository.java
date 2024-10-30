package com.sibi.GestionDeBibliotecas.Inventario.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
