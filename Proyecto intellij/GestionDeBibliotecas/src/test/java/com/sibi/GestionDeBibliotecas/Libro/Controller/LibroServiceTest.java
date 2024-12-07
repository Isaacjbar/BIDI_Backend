package com.sibi.GestionDeBibliotecas.Libro.Controller;

import com.sibi.GestionDeBibliotecas.Categoria.Model.Categoria;
import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaDTO;
import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaRepository;
import com.sibi.GestionDeBibliotecas.Libro.Model.Libro;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroDTO;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroRepository;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaRepository;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private LibroCategoriaRepository libroCategoriaRepository;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        // Caso de Prueba: Consultar libros registrados

        // Paso 1: Crear una lista de libros registrados.
        Libro libro1 = new Libro("El Principito", "Antoine de Saint-Exupéry", "El principito es una novela corta y la obra más famosa del escritor y aviador francés Antoine", 5);
        Libro libro2 = new Libro("El niño con el pijama de rayas", "John Boyne", "El niño con el pijama de rayas es una novela de ficción dramática ", 8);
        List<Libro> libros = Arrays.asList(libro1, libro2);

        // Paso 2: Configurar el mock del repositorio para devolver la lista de libros.
        when(libroRepository.findAll()).thenReturn(libros);

        // Paso 3: Llamar al método findAll() del servicio.
        ResponseEntity<Message> response = libroService.findAll();

        // Paso 4: Validar la respuesta obtenida
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Listado de libros", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }

    @Test
    void save() {
        // Caso de Prueba: Registro exitoso de un libro

        // Paso 1: Crear una categoría activa.
        Categoria categoria = new Categoria("Ficción");
        categoria.setStatus(Categoria.Status.ACTIVE);

        // Paso 2: Crear un objeto CategoriaDTO con los datos de la categoría.
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setCategoryId(1);
        categoriaDTO.setCategoryName("Ficción");

        // Paso 3: Crear un objeto LibroDTO con los datos del libro.
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitle("Cien Años de Soledad");
        libroDTO.setAuthor("Gabriel García Márquez");
        libroDTO.setDescription("Realismo mágico");
        libroDTO.setCopias(5);
        libroDTO.setCategorias(Arrays.asList(categoriaDTO));

        // Paso 4: Configurar los mocks para simular el comportamiento del sistema.
        when(categoriaRepository.findByCategoryIdAndStatus(1, Categoria.Status.ACTIVE)).thenReturn(categoria);
        Libro libro = new Libro("Cien Años de Soledad", "Gabriel García Márquez", "Realismo mágico", 5);
        when(libroRepository.saveAndFlush(any(Libro.class))).thenReturn(libro);

        // Paso 5: Llamar al método save() con el objeto LibroDTO.
        ResponseEntity<Message> response = libroService.save(libroDTO);

        // Paso 6: Validar la respuesta obtenida.
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El libro y sus categorías se registraron correctamente", response.getBody().getText());
        assertEquals(libro, response.getBody().getResult());
    }

    @Test
    void update() {
        // Caso de Prueba: Modificación exitosa de un libro

        // Paso 1: Crear una categoría activa.
        Categoria categoria = new Categoria("Ciencia Ficción");
        categoria.setStatus(Categoria.Status.ACTIVE);

        // Paso 2: Crear un objeto CategoriaDTO con los datos de la categoría.
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setCategoryId(1);
        categoriaDTO.setCategoryName("Ciencia Ficción");

        // Paso 3: Crear un objeto LibroDTO con los datos actualizados del libro.
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setBookId(1);
        libroDTO.setTitle("Dune");
        libroDTO.setAuthor("Frank Herbert");
        libroDTO.setDescription("Épica de ciencia ficción");
        libroDTO.setCopias(3);
        libroDTO.setCategorias(Arrays.asList(categoriaDTO));

        // Paso 4: Crear un objeto Libro con los datos originales del libro.
        Libro libro = new Libro(1L, "Dune", "Frank Herbert", "Épica de ciencia ficción", Libro.Status.ACTIVE, 3);

        // Paso 5: Configurar los mocks para simular el comportamiento del sistema.
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro));
        when(categoriaRepository.findByCategoryIdAndStatus(1, Categoria.Status.ACTIVE)).thenReturn(categoria);
        when(libroRepository.saveAndFlush(any(Libro.class))).thenReturn(libro);

        // Paso 6: Llamar al método update() con el objeto LibroDTO.
        ResponseEntity<Message> response = libroService.update(libroDTO);

        // Paso 7: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El libro y sus categorías se actualizaron correctamente", response.getBody().getText());
        assertEquals(libro, response.getBody().getResult());
    }

    @Test
    void changeStatus() {
        // Caso de Prueba: Desactivación exitosa de libro

        // Paso 1: Crear un objeto Libro con estado ACTIVO.
        Libro libro = new Libro(1L, "El Principito", "Antoine de Saint-Exupéry", "Fábula moderna", Libro.Status.ACTIVE, 10);

        // Paso 2: Crear un objeto LibroDTO con el nuevo estado INACTIVO.
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setBookId(1);
        libroDTO.setStatus(Libro.Status.INACTIVE);

        // Paso 3: Configurar los mocks para simular el comportamiento del sistema.
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro));
        when(libroRepository.saveAndFlush(any(Libro.class))).thenReturn(libro);

        // Paso 4: Llamar al método changeStatus() con el objeto LibroDTO.
        ResponseEntity<Message> response = libroService.changeStatus(libroDTO);

        // Paso 5: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El estado del libro se actualizó correctamente", response.getBody().getText());

        // Paso 6: Validar que el estado del libro cambió a INACTIVO.
        assertEquals(Libro.Status.INACTIVE, libro.getStatus());
    }

    @Test
    void findByStatusActive() {
        // Caso de Prueba: Consultar libros activos

        // Paso 1: Crear libros
        Libro libro1 = new Libro("Cien Años de Soledad", "Gabriel García Márquez", "Realismo mágico", 5);
        libro1.setStatus(Libro.Status.ACTIVE);

        Libro libro2 = new Libro("El Principito", "Antoine de Saint-Exupéry", "Fábula moderna", 10);
        libro2.setStatus(Libro.Status.ACTIVE);

        // Paso 2: Configurar el mock del repositorio para devolver los libros activos.
        List<Libro> libros = Arrays.asList(libro1, libro2);
        when(libroRepository.findByStatus(Libro.Status.ACTIVE)).thenReturn(libros);

        // Paso 3: Llamar al método findByStatus() del servicio con el estado "ACTIVE".
        ResponseEntity<Message> response = libroService.findByStatus("ACTIVE");

        // Paso 4: Validar la respuesta obtenida.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Listado de libros por estado", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }

    @Test
    void findByStatusInactive() {
        Libro libro1 = new Libro("Cien Años de Soledad", "Gabriel García Márquez", "Realismo mágico", 5);
        libro1.setStatus(Libro.Status.INACTIVE);

        Libro libro2 = new Libro("El Principito", "Antoine de Saint-Exupéry", "Fábula moderna", 10);
        libro2.setStatus(Libro.Status.INACTIVE);

        List<Libro> libros = Arrays.asList(libro1, libro2);

        when(libroRepository.findByStatus(Libro.Status.INACTIVE)).thenReturn(libros);

        ResponseEntity<Message> response = libroService.findByStatus("INACTIVE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Listado de libros por estado", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }
}