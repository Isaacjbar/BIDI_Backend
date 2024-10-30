package com.sibi.GestionDeBibliotecas.Prestamo.Controller;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoRepository;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Inventario.Model.Inventario;
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
public class PrestamoService {
    private static final Logger logger = LoggerFactory.getLogger(PrestamoService.class);

    private final PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        logger.info("La búsqueda de préstamos ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(prestamos, "Listado de préstamos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> save(PrestamoDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(dto.getUsuarioId());

        Inventario inventario = new Inventario();
        inventario.setInventarioId(dto.getInventarioId().intValue()); // Convert Long to Integer

        Prestamo prestamo = new Prestamo(usuario, inventario, dto.getFechaPrestamo(), dto.getFechaVencimiento(), Prestamo.Status.valueOf(dto.getEstado().toUpperCase()));
        prestamoRepository.saveAndFlush(prestamo);
        logger.info("El registro del préstamo ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(prestamo, "El préstamo se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Message> update(PrestamoDTO dto) {
        prestamoRepository.findById(dto.getPrestamoId().intValue()).ifPresentOrElse(
                prestamo -> {
                    Usuario usuario = new Usuario();
                    usuario.setUsuarioId(dto.getUsuarioId()); // Convert Long to Integer
                    prestamo.setUsuario(usuario);

                    Inventario inventario = new Inventario();
                    inventario.setInventarioId(dto.getInventarioId().intValue()); // Convert Long to Integer
                    prestamo.setInventario(inventario);

                    prestamo.setFechaPrestamo(dto.getFechaPrestamo());
                    prestamo.setFechaVencimiento(dto.getFechaVencimiento());
                    prestamo.setFechaDevolucion(dto.getFechaDevolucion());
                    prestamo.setStatus(Prestamo.Status.valueOf(dto.getEstado().toUpperCase()));
                    prestamoRepository.saveAndFlush(prestamo);
                    logger.info("La actualización del préstamo ha sido realizada correctamente");
                },
                () -> {
                    throw new IllegalArgumentException("El préstamo no existe");
                }
        );
        return new ResponseEntity<>(new Message("El préstamo se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> changeStatus(PrestamoDTO dto) {
        prestamoRepository.findById(dto.getPrestamoId().intValue()).ifPresentOrElse(
                prestamo -> {
                    prestamo.setStatus(prestamo.getStatus().equals(Prestamo.Status.ACTIVE) ? Prestamo.Status.ACTIVE : Prestamo.Status.INACTIVE);
                    prestamoRepository.saveAndFlush(prestamo);
                    logger.info("La actualización del estado del préstamo ha sido realizada correctamente");
                },
                () -> {
                    throw new IllegalArgumentException("El préstamo no existe");
                }
        );
        return new ResponseEntity<>(new Message("El estado del préstamo se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByStatus(String estado) {
        Prestamo.Status status;
        try {
            status = Prestamo.Status.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Message("Estado inválido", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        List<Prestamo> prestamos = prestamoRepository.findByStatus(status);
        logger.info("La búsqueda por estado de los préstamos ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(prestamos, "Listado de préstamos por estado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
