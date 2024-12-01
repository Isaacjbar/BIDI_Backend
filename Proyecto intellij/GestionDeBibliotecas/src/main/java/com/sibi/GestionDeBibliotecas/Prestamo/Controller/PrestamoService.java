package com.sibi.GestionDeBibliotecas.Prestamo.Controller;

import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaRepository;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaRepository;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoRepository;
import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PrestamoService {
    private static final Logger logger = LoggerFactory.getLogger(PrestamoService.class);

    private final LibroRepository libroRepository;

    private final UsuarioRepository usuarioRepository;

    private final PrestamoRepository prestamoRepository;

    private final JwtUtil jwtUtil; // Agrega JwtUtil aquí

    @Autowired
    public PrestamoService(LibroRepository libroRepository, UsuarioRepository usuarioRepository,
                           PrestamoRepository prestamoRepository, JwtUtil jwtUtil) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
        this.jwtUtil = jwtUtil; // Inicializa JwtUtil aquí
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        logger.info("La búsqueda de préstamos ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(prestamos, "Listado de préstamos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(PrestamoDTO dto) {
        // Buscar y validar el usuario
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(dto.getUsuarioId());
        if (!optionalUsuario.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = optionalUsuario.get();
        if (usuario.getEstado()!= Estado.ACTIVO) {
            return new ResponseEntity<>(new Message("El usuario está inactivo y no puede realizar préstamos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Buscar y validar el libro
        Optional<Libro> optionalLibro = libroRepository.findById(Math.toIntExact(dto.getLibroId()));
        if (!optionalLibro.isPresent()) {
            return new ResponseEntity<>(new Message("El libro no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Libro libro = optionalLibro.get();
        if (libro.getStatus() != Libro.Status.ACTIVE) {
            return new ResponseEntity<>(new Message("El libro está inactivo y no puede ser prestado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (libro.getCopias() <= 2) {
            return new ResponseEntity<>(new Message("El libro no tiene suficientes copias para ser prestado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Generar la fecha de préstamo (fecha actual)
        Date fechaPrestamo = new Date();

        // Calcular la fecha de vencimiento (10 días hábiles a partir de la fecha de préstamo)
        Date fechaVencimiento = calcularFechaVencimiento(fechaPrestamo, 10);

        // Crear y guardar el préstamo
        Prestamo prestamo = new Prestamo(usuario, libro, fechaPrestamo, fechaVencimiento);
        prestamoRepository.saveAndFlush(prestamo);

        // Actualizar el número de copias del libro
        libro.setCopias(libro.getCopias() - 1);
        libroRepository.saveAndFlush(libro);

        logger.info("El registro del préstamo ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(prestamo, "El préstamo se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> saveForCustomer(PrestamoDTO dto, String token) {

        // Extraer el ID del usuario autenticado desde el token
        Long userIdFromToken = jwtUtil.extractUserId(token);  // Asegúrate de pasar el token JWT al método

        // Verificar que el ID del usuario en el DTO coincida con el ID del token
        if (!userIdFromToken.equals(dto.getUsuarioId())) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar este préstamo con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }

        // Buscar y validar el usuario
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(dto.getUsuarioId());
        if (!optionalUsuario.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = optionalUsuario.get();
        if (usuario.getEstado() != Estado.ACTIVO) {
            return new ResponseEntity<>(new Message("El usuario está inactivo y no puede realizar préstamos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Buscar y validar el libro
        Optional<Libro> optionalLibro = libroRepository.findById(Math.toIntExact(dto.getLibroId()));
        if (!optionalLibro.isPresent()) {
            return new ResponseEntity<>(new Message("El libro no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Libro libro = optionalLibro.get();
        if (libro.getStatus() != Libro.Status.ACTIVE) {
            return new ResponseEntity<>(new Message("El libro está inactivo y no puede ser prestado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (libro.getCopias() <= 2) {
            return new ResponseEntity<>(new Message("El libro no tiene suficientes copias para ser prestado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Generar la fecha de préstamo (fecha actual)
        Date fechaPrestamo = new Date();

        // Calcular la fecha de vencimiento (10 días hábiles a partir de la fecha de préstamo)
        Date fechaVencimiento = calcularFechaVencimiento(fechaPrestamo, 10);

        // Crear y guardar el préstamo
        Prestamo prestamo = new Prestamo(usuario, libro, fechaPrestamo, fechaVencimiento);
        prestamoRepository.saveAndFlush(prestamo);

        // Actualizar el número de copias del libro
        libro.setCopias(libro.getCopias() - 1);
        libroRepository.saveAndFlush(libro);

        logger.info("El registro del préstamo ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(prestamo, "El préstamo se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    // Método para calcular la fecha de vencimiento sumando días hábiles
    private Date calcularFechaVencimiento(Date fechaInicio, int diasHabiles) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaInicio);
        int diasAgregados = 0;

        while (diasAgregados < diasHabiles) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
            if (diaSemana != Calendar.SATURDAY && diaSemana != Calendar.SUNDAY) {
                diasAgregados++;
            }
        }
        return calendar.getTime();
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(PrestamoDTO dto) {
        Optional<Prestamo> optionalPrestamo = prestamoRepository.findById(dto.getPrestamoId());

        if (!optionalPrestamo.isPresent()) {
            return new ResponseEntity<>(new Message("El préstamo no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Prestamo prestamo = optionalPrestamo.get();

        // Verificar si el estado ya es el solicitado
        if ((prestamo.getStatus().equals(Prestamo.Status.ACTIVE) && dto.getEstado().equalsIgnoreCase("ACTIVE")) ||
                (prestamo.getStatus().equals(Prestamo.Status.INACTIVE) && dto.getEstado().equalsIgnoreCase("INACTIVO"))) {

            String mensaje = String.format("El préstamo ya está en estado %s", prestamo.getStatus());
            logger.info(mensaje);
            return new ResponseEntity<>(new Message(mensaje, TypesResponse.SUCCESS), HttpStatus.OK);
        }

        Libro libro = prestamo.getLibro(); // Obtener el libro asociado al préstamo
        Prestamo.Status nuevoEstado;

        // Alterna el estado y ajusta las fechas y copias del libro según el nuevo estado
        if (prestamo.getStatus().equals(Prestamo.Status.ACTIVE)) {
            nuevoEstado = Prestamo.Status.INACTIVE;
            prestamo.setFechaDevolucion(new Date()); // Establece la fecha de devolución
            libro.setCopias(libro.getCopias() + 1); // Suma una copia al libro
        } else {
            nuevoEstado = Prestamo.Status.ACTIVE;
            prestamo.setFechaDevolucion(null); // Borra la fecha de devolución
            libro.setCopias(libro.getCopias() - 1); // Resta una copia al libro
        }

        prestamo.setStatus(nuevoEstado);
        prestamoRepository.saveAndFlush(prestamo);
        libroRepository.saveAndFlush(libro); // Actualiza el número de copias del libro

        logger.info("La actualización del estado del préstamo y las copias del libro ha sido realizada correctamente");
        String mensaje = String.format("El estado del préstamo se actualizó correctamente a %s", nuevoEstado);

        return new ResponseEntity<>(new Message(prestamo, mensaje,TypesResponse.SUCCESS), HttpStatus.OK);
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

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllByUsuario(Long userId,String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        // Extraer el ID del usuario autenticado desde el token
        Long userIdFromToken = jwtUtil.extractUserId(token);  // Asegúrate de pasar el token JWT al método
        // Verificar que el ID del usuario en el DTO coincida con el ID del token
        if (!userIdFromToken.equals(userId)) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar este préstamo con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }
        if (usuarioOpt.isEmpty()) {
            logger.warn("Usuario con ID " + userId + " no encontrado.");
            return new ResponseEntity<>(new Message(null, "Usuario no encontrado", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        List<Prestamo> prestamos = prestamoRepository.findByUsuario(usuarioOpt.get());
        if (prestamos.isEmpty()) {
            logger.info("No se encontraron préstamos para el usuario con ID: " + userId);
            return new ResponseEntity<>(new Message(null, "No se encontraron préstamos para el usuario", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        logger.info("La búsqueda de préstamos para el usuario con ID " + userId + " ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(prestamos, "Listado de préstamos del usuario", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
