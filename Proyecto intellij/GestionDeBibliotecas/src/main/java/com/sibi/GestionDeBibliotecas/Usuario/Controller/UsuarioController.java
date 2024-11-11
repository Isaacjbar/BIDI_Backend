package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Registrar usuario cliente
    @PostMapping("/register")
    public ResponseEntity<Message> save(@Validated(UsuarioDTO.Registrar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.save(dto);
    }

    // Consultar usuarios admin
    @GetMapping("/find-all")
    public ResponseEntity<Message> findAll() {
        return usuarioService.findAll();
    }

    // Complemento de consultar usuarios admin
    @GetMapping("/status/{estado}")
    public ResponseEntity<Message> findByStatus(@PathVariable String estado) {
        return usuarioService.findByStatus(estado);
    }

    // Modificar usuarios admin
    @PutMapping("/modify")
    public ResponseEntity<Message> modify(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.modify(dto);
    }

    // Cambiar estado de usuarios admin
    @PutMapping("/change-status/{id}")
    public ResponseEntity<Message> changeStatus(@Validated(UsuarioDTO.CambiarEstado.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.changeStatus(dto);
    }

    // Consultar perfil admin
    @GetMapping("/find/{id}")
    public ResponseEntity<Message> find(@Validated(UsuarioDTO.Consultar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.find(dto);
    }

    // Consultar perfil cliente
    @GetMapping("/find/for-customer/{id}")
    public ResponseEntity<Message> findForCustomer(@Validated(UsuarioDTO.Consultar.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.findForCustomer(dto, request.getHeader("Authorization"));
    }

    // Editar perfil admin
    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.update(dto);
    }

    // Editar perfil cliente
    @PutMapping("/update/for-customer")
    public ResponseEntity<Message> updateForCustomer(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.updateForCustomer(dto, request.getHeader("Authorization"));
    }

    // Recuperar contraseña cliente parte 1
    @PostMapping("/request-password-reset")
    public ResponseEntity<Message> requestPasswordReset(@Validated(UsuarioDTO.Solicitud.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.requestPasswordReset(dto);
    }

    // Recuperar contraseña cliente parte 2
    @PutMapping("/reset-password")
    public ResponseEntity<Message> resetPassword(@Validated(UsuarioDTO.Validacion.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.resetPassword(dto);
    }

    // Recuperar contraseña cliente desde el perfil
    @PutMapping("/reset-password-profile")
    public ResponseEntity<Message> resetPasswordProfile(@Validated(UsuarioDTO.Recuperacion.class) @RequestBody UsuarioDTO dto, HttpServletRequest request) {
        return usuarioService.resetPasswordProfile(dto, request.getHeader("Authorization"));
    }
}