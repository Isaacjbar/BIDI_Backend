package com.sibi.GestionDeBibliotecas.Libro.Controller;

import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaDTO;
import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroDTO;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroRepository;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoria;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaDTO;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaRepository;
import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;
import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaRepository;
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

    private final CategoriaRepository categoriaRepository;

    private final LibroCategoriaRepository libroCategoriaRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository, CategoriaRepository categoriaRepository, LibroCategoriaRepository libroCategoriaRepository) {
        this.libroRepository = libroRepository;
        this.categoriaRepository = categoriaRepository;
        this.libroCategoriaRepository = libroCategoriaRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Libro> libros = libroRepository.findAll();
        logger.info("La búsqueda de libros ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(libros, "Listado de libros", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> save(LibroDTO dto) {
        // Validación de campos
        if (dto.getTitle().length() > 255) {
            return new ResponseEntity<>(new Message("El título excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getAuthor().length() > 255) {
            return new ResponseEntity<>(new Message("El autor excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getDescription().length() > 250) {
            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el libro
        Libro libro = new Libro(dto.getTitle(), dto.getAuthor(), dto.getDescription());
        libro = libroRepository.saveAndFlush(libro);

        // Crear las relaciones en base a CategoriaDTO
        if (dto.getCategorias() != null) {
            for (CategoriaDTO categoriaDTO : dto.getCategorias()) {
                Categoria categoria = categoriaRepository.findById(categoriaDTO.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + categoriaDTO.getCategoryId()));

                // Crear y guardar la relación en LibroCategoria
                LibroCategoria libroCategoria = new LibroCategoria(libro, categoria);
                libroCategoriaRepository.save(libroCategoria);
            }
        }

        logger.info("El registro del libro y su asociación con categorías ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(libro, "El libro y sus categorías se registraron correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }


    @Transactional
    public ResponseEntity<Message> update(LibroDTO dto) {
        return libroRepository.findById(dto.getBookId())
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

                    // Actualiza la lista de categorías si no es null
                    if (dto.getCategorias() != null) {
                        // Eliminar todas las relaciones actuales entre el libro y sus categorías
                        libroCategoriaRepository.deleteAllByLibro(libro);

                        // Crear nuevas relaciones con las categorías proporcionadas en el DTO
                        for (CategoriaDTO categoriaDTO : dto.getCategorias()) {
                            Categoria categoria = categoriaRepository.findById(categoriaDTO.getCategoryId())
                                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + categoriaDTO.getCategoryId()));

                            // Crear y guardar la nueva relación
                            LibroCategoria libroCategoria = new LibroCategoria(libro, categoria);
                            libroCategoriaRepository.save(libroCategoria);
                        }
                    }

                    // Guarda los cambios en la base de datos
                    libroRepository.save(libro);
                    logger.info("La actualización del libro y sus categorías ha sido realizada correctamente");

                    // Retorna mensaje de éxito
                    return new ResponseEntity<>(new Message(libro, "El libro y sus categorías se actualizaron correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(new Message("El libro no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND));
    }


    @Transactional
    public ResponseEntity<Message> changeStatus(LibroDTO dto) {
        return libroRepository.findById(dto.getBookId()).map(libro -> {
            // Validar si el nuevo estado proporcionado en el DTO es diferente al estado actual
            if (dto.getStatus() != null && !dto.getStatus().equals(libro.getStatus())) {
                // Actualizar el estado solo si es diferente
                libro.setStatus(dto.getStatus());
                libroRepository.saveAndFlush(libro);
                logger.info("La actualización del estado del libro ha sido realizada correctamente. Nuevo estado: {}", libro.getStatus());
            } else {
                logger.info("No hubo cambios en el estado del libro, ya tenía el estado proporcionado. Estado actual: {}", libro.getStatus());
            }

            // Incluir el estado del libro en el mensaje de respuesta
            return new ResponseEntity<>(new Message("El estado del libro se actualizó correctamente. Estado actual: " + libro.getStatus(), TypesResponse.SUCCESS), HttpStatus.OK);
        }).orElseGet(() -> {
            throw new IllegalArgumentException("El libro no existe");
        });
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