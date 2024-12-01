package com.sibi.GestionDeBibliotecas.Controllers.Admin;

import com.sibi.GestionDeBibliotecas.Categoria.Controller.CategoriaService;
import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaDTO;
import com.sibi.GestionDeBibliotecas.Libro.Controller.LibroService;
import com.sibi.GestionDeBibliotecas.Libro.Model.LibroDTO;
import com.sibi.GestionDeBibliotecas.Libro_Categoria.Controller.LibroCategoriaService;
import com.sibi.GestionDeBibliotecas.Prestamo.Controller.PrestamoService;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Usuario.Controller.UsuarioService;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CategoriaService categoriaService;
    private final LibroCategoriaService libroCategoriaService;
    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;

    @Autowired
    public AdminController(CategoriaService categoriaService, LibroCategoriaService libroCategoriaService, LibroService libroService, PrestamoService prestamoService, UsuarioService usuarioService) {
        this.categoriaService = categoriaService;
        this.libroCategoriaService = libroCategoriaService;
        this.libroService = libroService;
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/category/all")
    public ResponseEntity<Message> getAllCategories() {
        return categoriaService.findAll();
    }

    @GetMapping("/category/status/{estado}")
    public ResponseEntity<Message> getCategoryByStatus(@PathVariable String estado) {
        return categoriaService.findByStatus(estado);
    }

    @PostMapping("/category/save")
    public ResponseEntity<Message> saveCategory(@Validated(CategoriaDTO.Registrar.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.save(dto);
    }

    @PutMapping("/category/update")
    public ResponseEntity<Message> updateCategory(@Validated(CategoriaDTO.Modificar.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.update(dto);
    }

    @PutMapping("/category/change-status")
    public ResponseEntity<Message> changeCategoryStatus(@Validated(CategoriaDTO.CambiarEstado.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.changeStatus(dto);
    }

    @GetMapping("/book-category/all")
    public ResponseEntity<Message> getAllBooksCategories() {
        return libroCategoriaService.findAll();
    }

    @GetMapping("/book/all")
    public ResponseEntity<Message> getAllBooks() {
        return libroService.findAll();
    }

    @GetMapping("/book/status/{estado}")
    public ResponseEntity<Message> getBookByStatus(@PathVariable String estado) {
        return libroService.findByStatus(estado);
    }

    @PostMapping("/book/save")
    public ResponseEntity<Message> saveBook(@Validated(LibroDTO.Registrar.class) @RequestBody LibroDTO dto) {
        return libroService.save(dto);
    }

    @PutMapping("/book/update")
    public ResponseEntity<Message> updateBook(@Validated(LibroDTO.Modificar.class) @RequestBody LibroDTO dto) {
        return libroService.update(dto);
    }

    @PutMapping("/book/change-status")
    public ResponseEntity<Message> changeBookStatus(@Validated(LibroDTO.CambiarEstado.class) @RequestBody LibroDTO dto) {
        return libroService.changeStatus(dto);
    }

    @GetMapping("/loan/all")
    public ResponseEntity<Message> getAllLoans() {
        return prestamoService.findAll();
    }

    @GetMapping("/loan/status/{estado}")
    public ResponseEntity<Message> getLoanByStatus(@PathVariable String estado) {
        return prestamoService.findByStatus(estado);
    }

    @PostMapping("/loan/save")
    public ResponseEntity<Message> saveLoan(@Validated(PrestamoDTO.Registrar.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.save(dto);
    }

    @PutMapping("/loan/change-status")
    public ResponseEntity<Message> changeLoanStatus(@Validated(PrestamoDTO.CambiarEstado.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.changeStatus(dto);
    }

    // Consultar usuarios admin
    @GetMapping("/user/find-all")
    public ResponseEntity<Message> findAllUsers() {
        return usuarioService.findAll();
    }

    // Cargar en el modal el perfil del usuario al editar
    @GetMapping("/user/find-id")
    public ResponseEntity<Message> findUser(@Validated(UsuarioDTO.Consultar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.findById(dto);
    }

    // Complemento de consultar usuarios admin
    @GetMapping("/user/status/{estado}")
    public ResponseEntity<Message> findUserByStatus(@PathVariable String estado) {
        return usuarioService.findByStatus(estado);
    }

    // Modificar usuarios admin
    @PutMapping("/user/modify")
    public ResponseEntity<Message> modifyUser(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.modify(dto);
    }

    // Cambiar estado de usuarios admin
    @PutMapping("/user/change-status")
    public ResponseEntity<Message> changeUserStatus(@Validated(UsuarioDTO.CambiarEstado.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.changeStatus(dto);
    }

    // Consultar perfil admin
    @GetMapping("/user/find")
    public ResponseEntity<Message> findUser(@Validated(UsuarioDTO.Consultar.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.find(dto, request.getHeader("Authorization"));
    }

    // Editar perfil admin
    @PutMapping("/user/update")
    public ResponseEntity<Message> updateUser(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.update(dto, request.getHeader("Authorization"));
    }
}