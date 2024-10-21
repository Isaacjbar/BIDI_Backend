package mx.edu.utez.controller;

import mx.edu.utez.model.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @Test
    void CreacionCategoria() {
        // Declarar instancia del controlador
        CategoryController controller = new CategoryController();

        // Llamar al método addCategory para agregar una nueva categoría
        String response = controller.addCategory(new Category(5, "Mathematics"));

        // Verificar si la categoría fue creada exitosamente
        List<Category> categories = controller.getAllCategories();
        assertTrue(categories.stream().anyMatch(cat -> "Mathematics".equals(cat.getCategoryName())));

        // Verificar el mensaje de éxito
        assertEquals("Categoría agregada exitosamente", response);
    }
    @Test
    void CategoriaDuplicada(){
        // Declarar instancia del controlador
        CategoryController controller = new CategoryController();

        // Agregar la primera categoría "Fiction"
        controller.addCategory(new Category(1, "Fiction"));

        // Intentar agregar una categoría duplicada con el mismo nombre
        String response = controller.addCategory(new Category(2, "Fiction"));

        // Verificar que el sistema rechaza la categoría duplicada
        assertEquals("La categoría ya existe", response);
    }
    @Test
    void CategoriaSinNombre(){
        // Declarar instancia del controlador
        CategoryController controller = new CategoryController();

        // Intentar agregar una categoría sin nombre
        String response = controller.addCategory(new Category(3, ""));

        // Verificar que el sistema no permite la creación sin nombre
        assertEquals("El nombre de la categoría no puede estar vacío", response);
    }
    @Test
    void LimiteCaracteres(){
        // Declarar instancia del controlador
        CategoryController controller = new CategoryController();

        // Crear un nombre de categoría con exactamente 100 caracteres
        String longName = "A".repeat(100);
        String response = controller.addCategory(new Category(4, longName));

        // Verificar que el sistema permite la creación
        assertEquals("Categoría agregada exitosamente", response);
    }
    @Test
    public void testCancelarCreacionCategoria() {
        // Declarar instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Inicia el proceso de creación de una categoría (simulamos cancelar la acción antes de agregar la categoría)
        // En este caso no se hace nada, ya que la acción de cancelar es simplemente no llamar al método addCategory().

        // Verificar que no se ha agregado ninguna categoría
        assertTrue(controller.getAllCategories().isEmpty(), "La lista de categorías no está vacía, cuando debería estarlo.");
    }
    @Test
    public void testConsultarTodasLasCategorias() {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Llama al método getAllCategories para obtener la lista de todas las categorías
        List<Category> categories = controller.getAllCategories();

        // Paso 3: Verifica que la lista contenga tanto categorías activas como inactivas
        assertNotNull(categories, "La lista de categorías no debe ser nula");
    }

    @Test
    public void testConsultarCategoriasActivas() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Agrega categorías (activas e inactivas) para realizar la consulta
        controller.addCategory(new Category(1, "Science"));
        controller.addCategory(new Category(2, "Technology"));

        // Desactivar la categoría "Technology" con ID 2
        controller.deactivateCategory(2);

        // Paso 2: Llama al método getCategoriesByStatus("active") para obtener solo las categorías activas
        List<Category> activeCategories = controller.getCategoriesByStatus("active");

        // Paso 3: Verifica que todas las categorías en la lista sean activas
        assertTrue(activeCategories.stream().allMatch(cat -> "active".equals(cat.getStatus())),
                "Todas las categorías deben estar activas");
    }
    @Test
    public void testConsultarCategoriasInactivas() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Agrega categorías (activas e inactivas) para realizar la consulta
        controller.addCategory(new Category(1, "Science"));
        controller.addCategory(new Category(2, "Technology"));

        // Desactivar la categoría "Technology" con ID 2
        controller.deactivateCategory(2);

        // Paso 2: Llama al método getCategoriesByStatus("inactive") para obtener solo las categorías inactivas
        List<Category> inactiveCategories = controller.getCategoriesByStatus("inactive");

        // Paso 3: Verifica que todas las categorías en la lista sean inactivas
        assertTrue(inactiveCategories.stream().allMatch(cat -> "inactive".equals(cat.getStatus())),
                "Todas las categorías deben estar inactivas");
    }


    @Test
    public void testConsultarCategoriasActivasSinResultados() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Agrega categorías inactivas para que no haya resultados activos
        controller.addCategory(new Category(1, "Science"));

        // Desactivar la categoría con ID 1
        controller.deactivateCategory(1);  // Desactivar todas las categorías

        // Paso 2: Llama al método getCategoriesByStatus("active") para obtener solo las categorías activas
        List<Category> activeCategories = controller.getCategoriesByStatus("active");

        // Paso 3: Verifica que no haya categorías activas en la lista
        assertTrue(activeCategories.isEmpty(), "No debe haber categorías activas");
    }
    @Test
    public void testConsultarCategoriasInactivasSinResultados() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Agrega categorías activas para que no haya resultados inactivos
        controller.addCategory(new Category(1, "Science"));

        // No desactivar ninguna categoría para que no haya inactivas
        // controller.deactivateCategory(1);  // No se desactiva ninguna categoría

        // Paso 2: Llama al método getCategoriesByStatus("inactive") para obtener solo las categorías inactivas
        List<Category> inactiveCategories = controller.getCategoriesByStatus("inactive");

        // Paso 3: Verifica que no haya categorías inactivas en la lista
        assertTrue(inactiveCategories.isEmpty(), "No debe haber categorías inactivas");
    }
    @Test
    public void testActualizarNombreCategoria() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega una categoría para actualizar
        controller.addCategory(new Category(1, "Science"));

        // Paso 3: Actualiza el nombre de la categoría
        String response = controller.updateCategory(1, new Category(1, "Mathematics"));

        // Paso 4: Verifica que el nombre se haya actualizado correctamente
        assertEquals("Categoría actualizada exitosamente.", response);

        // Paso 5: Verifica que el nombre de la categoría sea "Mathematics"
        Category updatedCategory = controller.getCategoryById(1);
        assertEquals("Mathematics", updatedCategory.getCategoryName(), "El nombre de la categoría debería ser 'Mathematics'");
    }
    @Test
    public void testActualizarCategoriaNombreDuplicado() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega dos categorías con nombres diferentes
        controller.addCategory(new Category(1, "Science"));
        controller.addCategory(new Category(2, "Technology"));

        // Paso 3: Intenta actualizar la categoría con un nombre que ya existe ("Technology")
        String response = controller.updateCategory(1, new Category(1, "Technology"));

        // Paso 4: Verifica que no se permita la actualización debido al nombre duplicado
        assertEquals("El nombre de la categoría ya está en uso.", response);
    }
    @Test
    public void testActualizarDescripcionCategoria() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega una categoría con una descripción vacía
        Category science = new Category(1, "Science");
        controller.addCategory(science);

        // Paso 3: Actualiza la descripción de la categoría
        science.setStatus("active");
        String response = controller.updateCategory(1, science);

        // Paso 4: Verifica que la categoría se haya actualizado correctamente
        assertEquals("Categoría actualizada exitosamente.", response);

        // Paso 5: Verifica que la descripción se haya actualizado
        Category updatedCategory = controller.getCategoryById(1);
        assertEquals("active", updatedCategory.getStatus(), "El estado de la categoría debería haber cambiado");
    }
    @Test
    public void testActualizarCategoriaInexistente() {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Intenta actualizar una categoría con un ID inexistente
        Exception exception = assertThrows(Exception.class, () -> {
            controller.updateCategory(99, new Category(99, "NonExistent"));
        });

        // Paso 3: Verifica que el sistema devuelva un mensaje adecuado
        assertEquals("Categoría no encontrada.", exception.getMessage());
    }


    @Test
    public void testActualizarCategoriaSinCambios() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega una categoría
        controller.addCategory(new Category(1, "Science"));

        // Paso 3: Intenta actualizar la categoría con el mismo nombre y descripción
        String response = controller.updateCategory(1, new Category(1, "Science"));

        // Paso 4: Verifica que el sistema permita la operación pero sin cambios
        assertEquals("Categoría actualizada exitosamente.", response);

        // Paso 5: Verifica que el nombre y descripción no hayan cambiado
        Category updatedCategory = controller.getCategoryById(1);
        assertEquals("Science", updatedCategory.getCategoryName(), "El nombre de la categoría no debería haber cambiado");
    }

    @Test
    public void testDesactivarCategoria() {
        // Crear instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Agregar categorías
        controller.addCategory(new Category(1, "Science"));
        controller.addCategory(new Category(2, "Technology"));

        // Intentar desactivar la categoría con ID 2
        String response = null;
        try {
            response = controller.deactivateCategory(2);  // Desactivar la categoría con ID 2
        } catch (Exception e) {
            fail("No se pudo desactivar la categoría: " + e.getMessage());
        }

        // Verificar que el mensaje sea el correcto
        assertEquals("Categoría desactivada.", response);
    }
    @Test
    public void testDesactivacionExitosaCategoriaActiva() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega una categoría activa
        controller.addCategory(new Category(1, "Science"));

        // Paso 3: Desactiva la categoría
        String response = controller.deactivateCategory(1);

        // Paso 4: Verifica que el estado de la categoría sea "inactive"
        Category category = controller.getCategoryById(1);
        assertEquals("inactive", category.getStatus(), "La categoría debería estar inactiva.");

        // Paso 5: Verifica que el sistema devuelva el mensaje correcto
        assertEquals("Categoría desactivada.", response, "El mensaje debería indicar que la categoría fue desactivada.");
    }

    @Test
    public void testDesactivarCategoriaInexistente() {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Intenta desactivar una categoría con un ID inexistente
        Exception exception = assertThrows(Exception.class, () -> {
            controller.deactivateCategory(99);
        });

        // Paso 3: Verifica que el sistema devuelva el mensaje adecuado
        assertEquals("Categoría no encontrada.", exception.getMessage(), "El mensaje debería indicar que la categoría no fue encontrada.");
    }
    @Test
    public void testDesactivarCategoriaYaInactiva() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega una categoría y desactívala
        controller.addCategory(new Category(1, "Science"));
        controller.deactivateCategory(1);

        // Paso 3: Intenta desactivar nuevamente la categoría
        String response = controller.deactivateCategory(1);

        // Paso 4: Verifica que el estado de la categoría siga siendo "inactive"
        Category category = controller.getCategoryById(1);
        assertEquals("inactive", category.getStatus(), "La categoría ya debería estar inactiva.");

        // Paso 5: Verifica que el sistema devuelva el mensaje correcto
        assertEquals("Categoría desactivada.", response, "El mensaje debería indicar que la categoría fue desactivada.");
    }

    @Test
    public void testVerificacionCategoriaDesactivada() throws Exception {
        // Paso 1: Declara una instancia de CategoryController
        CategoryController controller = new CategoryController();

        // Paso 2: Agrega una categoría y desactívala
        controller.addCategory(new Category(1, "Science"));
        controller.deactivateCategory(1);

        // Paso 3: Intenta desactivar nuevamente la categoría
        String response = controller.deactivateCategory(1);

        // Paso 4: Verifica que el estado de la categoría siga siendo "inactive"
        Category category = controller.getCategoryById(1);
        assertEquals("inactive", category.getStatus(), "La categoría ya debería estar inactiva.");

        // Paso 5: Verifica que el sistema devuelva el mensaje correcto
        assertEquals("Categoría desactivada.", response, "El mensaje debería indicar que la categoría fue desactivada.");
    }

}






