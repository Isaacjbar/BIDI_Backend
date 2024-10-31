package com.sibi.GestionDeBibliotecas.Categoria.Controller;

import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;
import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaDTO;
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
public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        logger.info("La búsqueda de categorías ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(categorias, "Listado de categorías", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> save(CategoriaDTO dto) {
        if (dto.getCategoryName().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Categoria categoria = new Categoria(dto.getCategoryName());
        categoriaRepository.save(categoria);
        logger.info("El registro de la categoría ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(categoria, "La categoría se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Message> update(CategoriaDTO dto) {
        return categoriaRepository.findById(dto.getCategoryId())
                .map(categoria -> {
                    // Actualiza solo si el valor no es null en el DTO
                    if (dto.getCategoryName() != null) {
                        if (dto.getCategoryName().length() > 100) {
                            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
                        }
                        categoria.setCategoryName(dto.getCategoryName());
                    }
                    if (dto.getStatus() != null) {
                        categoria.setStatus(dto.getStatus());
                    }
                    // Guarda los cambios en la base de datos
                    categoriaRepository.save(categoria);
                    logger.info("La actualización de la categoría ha sido realizada correctamente");
                    return new ResponseEntity<>(new Message(categoria, "La categoría se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(new Message("La categoría no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND));
    }

    @Transactional
    public ResponseEntity<Message> changeStatus(CategoriaDTO dto) {
        return categoriaRepository.findById(dto.getCategoryId())
                .map(categoria -> {
                    categoria.setStatus(!categoria.getStatus().equals(Categoria.Status.INACTIVE) ? Categoria.Status.INACTIVE : Categoria.Status.ACTIVE);
                    categoriaRepository.save(categoria);
                    logger.info("La actualización del estado de la categoría ha sido realizada correctamente");
                    return new ResponseEntity<>(new Message(categoria, "El estado de la categoría se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(new Message("La categoría no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND));
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByStatus(String estado) {
        Categoria.Status status;
        try {
            status = Categoria.Status.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Message("Estado inválido", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        List<Categoria> categorias = categoriaRepository.findByStatus(status);
        logger.info("La búsqueda de categorías por estado ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(categorias, "Listado de categorías por estado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
