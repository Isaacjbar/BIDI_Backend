package com.sibi.GestionDeBibliotecas.Controllers.Global;

import com.sibi.GestionDeBibliotecas.Usuario.Controller.UsuarioService;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/global")
public class GlobalController {

    private final UsuarioService usuarioService;

    @Autowired
    public GlobalController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Registrar usuario cliente
    @PostMapping("/register")
    public ResponseEntity<Message> register(@Validated(UsuarioDTO.Registrar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.save(dto);
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