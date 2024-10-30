package com.sibi.GestionDeBibliotecas.Libro_Categoria.Model;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LibroCategoriaRepository extends JpaRepository<LibroCategoria, Integer> {
}