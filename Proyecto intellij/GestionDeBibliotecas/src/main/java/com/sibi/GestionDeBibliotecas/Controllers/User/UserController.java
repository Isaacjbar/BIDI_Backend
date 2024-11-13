package com.sibi.GestionDeBibliotecas.Controllers.User;

import com.sibi.GestionDeBibliotecas.Libro.Controller.LibroService;
import com.sibi.GestionDeBibliotecas.Prestamo.Controller.PrestamoService;
import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Usuario.Controller.UsuarioService;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class UserController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    @Autowired
    public UserController(PrestamoService prestamoService, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/loan/save/for-customer")
    public ResponseEntity<Message> saveForCustomer(@RequestBody PrestamoDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);  // Eliminar "Bearer "
        return prestamoService.saveForCustomer(dto, token);
    }

    @GetMapping("/book/for-customer")
    public ResponseEntity<Message> getAllForCustomer() {
        return libroService.findByStatus("ACTIVE");
    }

    // Consultar perfil cliente
    @GetMapping("/user/find/for-customer")
    public ResponseEntity<Message> findForCustomer(@Validated(UsuarioDTO.Consultar.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.findForCustomer(dto, request.getHeader("Authorization"));
    }

    // Editar perfil cliente
    @PutMapping("/user/update/for-customer")
    public ResponseEntity<Message> updateForCustomer(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.updateForCustomer(dto, request.getHeader("Authorization"));
    }
}