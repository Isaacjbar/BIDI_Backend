package com.sibi.GestionDeBibliotecas.Libro.Controller;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroDTO;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroRepository;
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
public class LibroService {
    private static final Logger logger = LoggerFactory.getLogger(LibroService.class);

    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Libro> libros = libroRepository.findAll();
        logger.info("La búsqueda de libros ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(libros, "Listado de libros", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> save(LibroDTO dto) {
        if (dto.getTitle().length() > 255) {
            return new ResponseEntity<>(new Message("El título excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getAuthor().length() > 255) {
            return new ResponseEntity<>(new Message("El autor excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getDescription().length() > 250) {
            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Libro libro = new Libro(dto.getTitle(), dto.getAuthor(), dto.getDescription());
        libroRepository.saveAndFlush(libro);
        logger.info("El registro del libro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(libro, "El libro se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Message> update(LibroDTO dto) {
        return libroRepository.findById(dto.getLibroId())
                .map(libro -> {
                    // Valida el campo `title` solo si no es null y dentro del límite de caracteres
                    if (dto.getTitle() != null) {
                        if (dto.getTitle().length() > 255) {
                            return new ResponseEntity<>(new Message("El título excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
                        }
                        libro.setTitle(dto.getTitle());
                    }

                    // Valida el campo `author` solo si no es null y dentro del límite de caracteres
                    if (dto.getAuthor() != null) {
                        if (dto.getAuthor().length() > 255) {
                            return new ResponseEntity<>(new Message("El autor excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
                        }
                        libro.setAuthor(dto.getAuthor());
                    }

                    // Valida el campo `description` solo si no es null y dentro del límite de caracteres
                    if (dto.getDescription() != null) {
                        if (dto.getDescription().length() > 250) {
                            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
                        }
                        libro.setDescription(dto.getDescription());
                    }

                    // Guarda los cambios en la base de datos
                    libroRepository.save(libro);
                    logger.info("La actualización del libro ha sido realizada correctamente");

                    // Retorna mensaje de éxito
                    return new ResponseEntity<>(new Message(libro, "El libro se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(new Message("El libro no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND));
    }

    @Transactional
    public ResponseEntity<Message> changeStatus(LibroDTO dto) {
        libroRepository.findById(dto.getLibroId()).ifPresentOrElse(
                libro -> {
                    libro.setStatus(!libro.getStatus().equals(Libro.Status.INACTIVE) ? Libro.Status.INACTIVE : Libro.Status.ACTIVE);
                    libroRepository.saveAndFlush(libro);
                    logger.info("La actualización del estado del libro ha sido realizada correctamente");
                },
                () -> {
                    throw new IllegalArgumentException("El libro no existe");
                }
        );
        return new ResponseEntity<>(new Message("El estado del libro se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByStatus(String estado) {
        Libro.Status status;
        try {
            status = Libro.Status.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Message("Estado inválido", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        List<Libro> libros = libroRepository.findByStatus(status);
        logger.info("La búsqueda de libros por estado ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(libros, "Listado de libros por estado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}