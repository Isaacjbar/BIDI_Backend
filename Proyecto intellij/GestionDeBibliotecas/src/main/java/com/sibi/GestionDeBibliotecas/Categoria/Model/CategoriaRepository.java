package com.sibi.GestionDeBibliotecas.Categoria.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByStatus(Categoria. Status status);
    Categoria findByCategoryIdAndStatus(Integer id, Categoria. Status status);
}
