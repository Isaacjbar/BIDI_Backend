package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/find-all")
    public ResponseEntity<Message> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/find-user/{id}")
    public ResponseEntity<Message> findById(@Validated(UsuarioDTO.Consultar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.findById(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<Message> save(@Validated(UsuarioDTO.Registrar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.update(dto);
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Message> changeStatus(@Validated(UsuarioDTO.CambiarEstado.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.changeStatus(dto);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<Message> requestPasswordReset(@Validated(UsuarioDTO.Solicitud.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.requestPasswordReset(dto);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Message> validateToken(@Validated(UsuarioDTO.Validacion.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.validateToken(dto);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Message> resetPassword(@Validated(UsuarioDTO.Recuperacion.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.resetPassword(dto);
    }
}