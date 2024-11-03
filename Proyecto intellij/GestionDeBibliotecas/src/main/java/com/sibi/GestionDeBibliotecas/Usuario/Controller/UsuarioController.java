package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
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

    @PreAuthorize("hasAuthority(T(com.sibi.GestionDeBibliotecas.Util.Enum.Rol).ADMINISTRADOR.name())")
    @GetMapping("/find-all")
    public ResponseEntity<Message> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/find-user/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(UsuarioDTO.Registrar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(UsuarioDTO.Modificar.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.update(dto);
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Message> changeStatus(@Validated(UsuarioDTO.CambiarEstado.class) @PathVariable Long id) {
        return usuarioService.changeStatus(id);
    }
}