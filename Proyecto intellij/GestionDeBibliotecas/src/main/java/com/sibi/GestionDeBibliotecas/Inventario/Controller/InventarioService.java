package com.sibi.GestionDeBibliotecas.Inventario.Controller;

import com.sibi.GestionDeBibliotecas.Inventario.Model.Inventario;
import com.sibi.GestionDeBibliotecas.Inventario.Model.InventarioDTO;
import com.sibi.GestionDeBibliotecas.Inventario.Model.InventarioRepository;
import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
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
public class InventarioService {
    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository inventarioRepository;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Inventario> inventarios = inventarioRepository.findAll();
        logger.info("La búsqueda de inventarios ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(inventarios, "Listado de inventarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> save(InventarioDTO dto) {
        Inventario inventario = new Inventario();
        Libro libro = new Libro();
        libro.setBookId(dto.getInventarioId().longValue()); // Parse Integer to Long
        inventario.setLibro(libro);
        inventario.setCopiasDisponibles(dto.getCopiasDisponibles());
        inventario.setCopiasTotales(dto.getCopiasTotales());
        inventario.setStatus(Inventario.Status.valueOf(dto.getEstado().toUpperCase()));

        inventarioRepository.saveAndFlush(inventario);
        logger.info("El registro del inventario ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(inventario, "El inventario se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Message> update(InventarioDTO dto) {
        inventarioRepository.findById(dto.getInventarioId()).ifPresentOrElse(
                inventario -> {
                    inventario.setCopiasDisponibles(dto.getCopiasDisponibles());
                    inventario.setCopiasTotales(dto.getCopiasTotales());
                    inventario.setStatus(Inventario.Status.valueOf(dto.getEstado().toUpperCase()));
                    inventarioRepository.saveAndFlush(inventario);
                    logger.info("La actualización del inventario ha sido realizada correctamente");
                },
                () -> {
                    throw new IllegalArgumentException("El inventario no existe");
                }
        );
        return new ResponseEntity<>(new Message("El inventario se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> changeStatus(InventarioDTO dto) {
        inventarioRepository.findById(dto.getInventarioId()).ifPresentOrElse(
                inventario -> {
                    inventario.setStatus(inventario.getStatus().equals(Inventario.Status.INACTIVE) ? Inventario.Status.ACTIVE : Inventario.Status.INACTIVE);
                    inventarioRepository.saveAndFlush(inventario);
                    logger.info("La actualización del estado del inventario ha sido realizada correctamente");
                },
                () -> {
                    throw new IllegalArgumentException("El inventario no existe");
                }
        );
        return new ResponseEntity<>(new Message("El estado del inventario se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByStatus(String estado) {
        Inventario.Status status;
        try {
            status = Inventario.Status.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Message("Estado inválido", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        List<Inventario> inventarios = inventarioRepository.findByStatus(status);
        logger.info("La búsqueda por estado de los inventarios ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(inventarios, "Listado de inventarios por estado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
