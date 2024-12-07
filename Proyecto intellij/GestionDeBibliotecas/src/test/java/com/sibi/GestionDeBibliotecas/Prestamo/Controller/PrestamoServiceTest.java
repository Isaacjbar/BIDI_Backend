package com.sibi.GestionDeBibliotecas.Prestamo.Controller;

import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroRepository;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoRepository;
import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void setId(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
/*Crear un objeto Usuario con estado ACTIVO y un ID válido.
Crear un objeto Libro con al menos 5 copias disponibles y estado ACTIVE.
Crear un objeto DTO que contenga los IDs del usuario y del libro.
Simular la búsqueda del usuario y del libro en los repositorios mediante sus IDs.
Simular el guardado del préstamo en el repositorio utilizando el método saveAndFlush.
Llamar al método save() del servicio para registrar el préstamo con el DTO proporcionado.

*/
    @Test
    void registrarPrestamo() throws Exception {
        Usuario usuario = new Usuario();
        setId(usuario, "usuarioId", 1L); // Establece el ID del usuario
        usuario.setEstado(Estado.ACTIVO);

        Libro libro = new Libro();
        setId(libro, "bookId", 1L); // Establece el ID del libro
        libro.setCopias(5);
        libro.setStatus(Libro.Status.ACTIVE);

        PrestamoDTO dto = new PrestamoDTO();
        dto.setUsuarioId(1L);
        dto.setLibroId(1L);

        Prestamo prestamo = new Prestamo(usuario, libro, new Date(), new Date());
        setId(prestamo, "prestamoId", 1); // Establece el ID del préstamo

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro));
        when(prestamoRepository.saveAndFlush(any(Prestamo.class))).thenReturn(prestamo);

        ResponseEntity<Message> response = prestamoService.save(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("El préstamo se registró correctamente", response.getBody().getText());
        assertEquals(Prestamo.Status.ACTIVE, ((Prestamo) response.getBody().getResult()).getStatus());
    }
    /*
    * Crear un objeto Usuario con un ID válido.
Crear un objeto Libro con un ID válido.
Crear dos objetos Prestamo asociados al usuario y al libro con fechas de inicio y fin válidas.
Simular el comportamiento del repositorio para devolver una lista con los dos préstamos creados.
Llamar al método findAll() del servicio para consultar todos los préstamos registrados
*/

    @Test
    void consultarPrestamos() throws Exception {
        Usuario usuario = new Usuario();
        setId(usuario, "usuarioId", 1L);

        Libro libro = new Libro();
        setId(libro, "bookId", 1L);

        Prestamo prestamo1 = new Prestamo(usuario, libro, new Date(), new Date());
        setId(prestamo1, "prestamoId", 1);

        Prestamo prestamo2 = new Prestamo(usuario, libro, new Date(), new Date());
        setId(prestamo2, "prestamoId", 2);

        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);

        when(prestamoRepository.findAll()).thenReturn(prestamos);

        ResponseEntity<Message> response = prestamoService.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Listado de préstamos", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }
/*
* Crear un objeto Usuario con un ID válido.
Crear un objeto Libro con 3 copias disponibles y un ID válido.
Crear un objeto Prestamo con estado ACTIVE, asociado al usuario y al libro, y un ID válido.
Crear un objeto DTO con el ID del préstamo y el nuevo estado (INACTIVE).
Simular la búsqueda del préstamo en el repositorio mediante su ID.
Simular la actualización del préstamo en el repositorio utilizando el método saveAndFlush.
Simular la actualización del libro en el repositorio para incrementar el número de copias disponibles.
Llamar al método changeStatus() del servicio para cambiar el estado del préstamo.
Verificar que el estado de la respuesta sea 200.
Verificar que el cuerpo de la respuesta no sea nulo.
Comprobar que el mensaje de respuesta sea "El estado del préstamo se actualizó correctamente".
*/
    @Test
    void cambiarEstadoPrestamo() throws Exception {
        Usuario usuario = new Usuario();
        setId(usuario, "usuarioId", 1L);

        Libro libro = new Libro();
        setId(libro, "bookId", 1L);
        libro.setCopias(3);

        Prestamo prestamo = new Prestamo(usuario, libro, new Date(), new Date());
        setId(prestamo, "prestamoId", 1);
        prestamo.setStatus(Prestamo.Status.ACTIVE);

        PrestamoDTO dto = new PrestamoDTO();
        dto.setPrestamoId(1);
        dto.setEstado("INACTIVE");

        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));
        when(prestamoRepository.saveAndFlush(any(Prestamo.class))).thenReturn(prestamo);
        when(libroRepository.saveAndFlush(any(Libro.class))).thenReturn(libro);

        ResponseEntity<Message> response = prestamoService.changeStatus(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getText().contains("El estado del préstamo se actualizó correctamente"));
    }
}
