package mx.edu.utez.controller;

import mx.edu.utez.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTest {

    private BookController controller;

    @BeforeEach
    void setUp() {
        controller = new BookController();
    }

    // M301 Caso 1: Registro exitoso de un libro
    @Test
    void testAddBook_Success() {
        Book libro = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");

        String resultado = controller.addBook(libro);

        assertEquals("Libro agregado exitosamente.", resultado);
    }

    // M301 Caso 2: Registro fallido por título duplicado
    @Test
    void testAddBook_Failure_DuplicateTitle() {
        // Agregar el primer libro
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libro1);

        // Intentar agregar un segundo libro con el mismo título
        Book libro2 = new Book("Otro Autor", "Cien años de soledad", "Cuento", "Activo", "Otra descripción");

        String resultado = controller.addBook(libro2);

        assertEquals("Este libro ya está registrado.", resultado);
    }

    // M301 Caso 3: Registro fallido por datos faltantes (sin título)
    @Test
    void testAddBook_Failure_MissingTitle() {
        // Faltan el título
        Book libro = new Book("Gabriel García Márquez", null, "Novela", "Activo", "Obra literaria del Boom Latinoamericano");

        String resultado = controller.addBook(libro);

        // Se espera un error de campo obligatorio
        assertEquals("El título es un campo obligatorio.", resultado);
    }

    // Pruebas para Consultar Todos los Libros
    // M302 Caso 1: Consulta exitosa de todos los libros por el administrador
    @Test
    void testGetAllBooks_MultipleBooks() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Novela", "Inactivo", "Obra literaria");
        Book libro3 = new Book("Gabriel García Márquez", "Crónica de una muerte anunciada", "Novela", "Activo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);
        controller.addBook(libro3);

        List<Book> books = controller.getAllBooks();
        assertEquals(3, books.size());
    }

    // M302 Caso 2: Consulta cuando no hay libros registrados
    @Test
    void testGetAllBooks_EmptyList() {
        List<Book> books = controller.getAllBooks();
        assertTrue(books.isEmpty());
    }

    // M302 Caso 3: Consultar todos los libros con un solo libro registrado
    @Test
    void testGetAllBooks_SingleBook() {
        Book libro = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libro);

        List<Book> books = controller.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Cien años de soledad", books.get(0).getTitle());
    }

    // Pruebas para Consultar Libros Activos
    // M303 Caso 1: Consultar libros activos
    @Test
    void testGetBooksByStatus_ActiveBooks() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Novela", "Inactivo", "Obra literaria");
        Book libro3 = new Book("Gabriel García Márquez", "Crónica de una muerte anunciada", "Novela", "Activo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);
        controller.addBook(libro3);

        List<Book> activeBooks = controller.getBooksByStatus("Activo");
        assertEquals(2, activeBooks.size());
        assertTrue(activeBooks.stream().allMatch(book -> book.getStatus().equals("Activo")));
    }

    // M303 Caso 2: Consultar libros inactivos
    @Test
    void testGetBooksByStatus_AllInactive() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Inactivo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Novela", "Inactivo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);

        List<Book> activeBooks = controller.getBooksByStatus("Activo");
        assertTrue(activeBooks.isEmpty());
    }

    // M303 Caso 3: Consultar libros activos con un solo libro activo
    @Test
    void testGetBooksByStatus_SingleActive() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Novela", "Inactivo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);

        List<Book> activeBooks = controller.getBooksByStatus("Activo");
        assertEquals(1, activeBooks.size());
        assertEquals("Cien años de soledad", activeBooks.get(0).getTitle());
    }

    // Pruebas para Consultar Libros por Categoría
    // M304 Caso 1: Consultar libros por categoría con varios libros en la lista
    @Test
    void testGetBooksByCategory_MultipleResults() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Novela", "Inactivo", "Obra literaria");
        Book libro3 = new Book("Gabriel García Márquez", "Crónica de una muerte anunciada", "Ensayo", "Activo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);
        controller.addBook(libro3);

        List<Book> novelaBooks = controller.getBooksByCategory("Novela");
        assertEquals(2, novelaBooks.size());
        assertTrue(novelaBooks.stream().allMatch(book -> book.getCategory().equals("Novela")));
    }

    // M304 Caso 2: Consultar libros sin categorías coincidentes
    @Test
    void testGetBooksByCategory_EmptyCategory() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Novela", "Inactivo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);

        List<Book> cienciaFiccionBooks = controller.getBooksByCategory("Ciencia Ficción");
        assertTrue(cienciaFiccionBooks.isEmpty());
    }

    // M304 Caso 3: Consultar un solo libro en la lista
    @Test
    void testGetBooksByCategory_SingleResult() {
        Book libro1 = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        Book libro2 = new Book("Gabriel García Márquez", "El amor en los tiempos del cólera", "Cuento", "Inactivo", "Obra literaria");

        controller.addBook(libro1);
        controller.addBook(libro2);

        List<Book> novelaBooks = controller.getBooksByCategory("Novela");
        assertEquals(1, novelaBooks.size());
        assertEquals("Cien años de soledad", novelaBooks.get(0).getTitle());
    }

    // Pruebas para Actualización de libros
    // M305 Caso 1: Modificación exitosa de un libro
    @Test
    void testUpdateBook_Success() {
        Book libroOriginal = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libroOriginal);

        Book libroActualizado = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Inactivo", "Edición especial");

        String resultado = controller.updateBook("Cien años de soledad", libroActualizado);
        assertEquals("Libro actualizado exitosamente.", resultado);

        Book libro = controller.getBookByTitle("Cien años de soledad");
        assertEquals("Inactivo", libro.getStatus());
        assertEquals("Edición especial", libro.getDescription());
    }

    // M305 Caso 2: Actualización fallida por libro no encontrado
    @Test
    void testUpdateBook_BookNotFound() {
        Book libroOriginal = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libroOriginal);

        Book libroActualizado = new Book("Gabriel García Márquez", "Otro título", "Novela", "Inactivo", "Edición especial");
        String resultado = controller.updateBook("Otro título", libroActualizado);

        assertEquals("Libro no encontrado.", resultado);
    }

    // M305 Caso 3: Actualización fallida por datos inválidos
    @Test
    void testUpdateBook_InvalidData() {
        Book libroOriginal = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libroOriginal);

        Book libroActualizado = new Book("Gabriel García Márquez", "", "Novela", "Pendiente", "Descripción inválida");

        String resultado = controller.updateBook("", libroActualizado);
        assertEquals("Libro no encontrado.", resultado);
    }

    // Pruebas para la Desactivación de libros
    // M306 Caso 1: Desactivación exitosa
    @Test
    void testDeactivateBook_Success() {
        Book libro = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Activo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libro);

        String resultado = controller.deactivateBook("Cien años de soledad");
        assertEquals("Libro desactivado exitosamente.", resultado);

        Book desactivado = controller.getBookByTitle("Cien años de soledad");
        assertEquals("Inactivo", desactivado.getStatus());
    }

    // M305 Caso 2: Desactivación fallida por libro no encontrado
    @Test
    void testDeactivateBook_BookNotFound() {
        String resultado = controller.deactivateBook("Cien años de soledad");
        assertEquals("Libro no encontrado.", resultado);
    }

    // M305 Caso 3: Desactivación fallida por libro ya inactivo
    @Test
    void testDeactivateBook_AlreadyInactive() throws Exception {
        Book libro = new Book("Gabriel García Márquez", "Cien años de soledad", "Novela", "Inactivo", "Obra literaria del Boom Latinoamericano");
        controller.addBook(libro);

        String resultado = controller.deactivateBook("Cien años de soledad");
        assertEquals("El libro ya está inactivo.", resultado);
    }
}