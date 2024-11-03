package com.sibi.GestionDeBibliotecas.Libro_Categoria.Controller;

import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaRepository;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class LibroCategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(LibroCategoriaService.class);

    private final LibroCategoriaRepository libroCategoriaRepository;

    @Autowired
    public LibroCategoriaService(LibroCategoriaRepository libroCategoriaRepository) {
        this.libroCategoriaRepository = libroCategoriaRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<LibroCategoria> libroCategorias = libroCategoriaRepository.findAll();
        logger.info("La búsqueda de relaciones libro-categoría ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(libroCategorias, "Listado de relaciones libro-categoría", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}