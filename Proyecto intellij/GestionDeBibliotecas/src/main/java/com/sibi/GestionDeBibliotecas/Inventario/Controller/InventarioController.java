package com.sibi.GestionDeBibliotecas.Inventario.Controller;

import com.sibi.GestionDeBibliotecas.Inventario.Model.InventarioDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventarioController {

    private final InventarioService inventarioService;

    @Autowired
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAll() {
        return inventarioService.findAll();
    }

    @GetMapping("/status/{estado}")
    public ResponseEntity<Message> getByStatus(@PathVariable String estado) {
        return inventarioService.findByStatus(estado);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(InventarioDTO.Registrar.class) @RequestBody InventarioDTO dto) {
        return inventarioService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(InventarioDTO.Modificar.class) @RequestBody InventarioDTO dto) {
        return inventarioService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(InventarioDTO.CambiarEstado.class) @RequestBody InventarioDTO dto) {
        return inventarioService.changeStatus(dto);
    }
}