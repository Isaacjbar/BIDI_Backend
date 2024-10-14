package com.sibi.GestionDeBibliotecas.Libro.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    List<Libro> findByTitleContaining(String title);

    List<Libro> findByAuthorContaining(String author);

    List<Libro> findByStatus(Libro.Status status);

    Libro findByIsbn(String isbn);
}