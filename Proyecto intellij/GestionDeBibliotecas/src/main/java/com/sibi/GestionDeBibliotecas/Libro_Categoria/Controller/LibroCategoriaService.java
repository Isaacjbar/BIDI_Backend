package com.sibi.GestionDeBibliotecas.Libro_Categoria.Controller;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaDTO;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaRepository;
import com.sibi.GestionDeBibliotecas.Util.Message;
import com.sibi.GestionDeBibliotecas.Util.TypesResponse;
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

    @Transactional
    public ResponseEntity<Message> save(LibroCategoriaDTO dto) {
        Libro libro = new Libro();
        libro.setBookId(dto.getLibroId());
        Categoria categoria = new Categoria();
        categoria.setCategoryId(dto.getCategoriaId());

        LibroCategoria libroCategoria = new LibroCategoria(libro, categoria);
        libroCategoriaRepository.saveAndFlush(libroCategoria);
        logger.info("El registro de la relación libro-categoría ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(libroCategoria, "La relación libro-categoría se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }
}