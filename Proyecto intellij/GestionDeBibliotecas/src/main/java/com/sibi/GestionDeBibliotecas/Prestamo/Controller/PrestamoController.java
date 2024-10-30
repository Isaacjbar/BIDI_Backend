package com.sibi.GestionDeBibliotecas.Prestamo.Controller;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class PrestamoController {

    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAll() {
        return prestamoService.findAll();
    }

    @GetMapping("/status/{estado}")
    public ResponseEntity<Message> getByStatus(@PathVariable String estado) {
        return prestamoService.findByStatus(estado);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(PrestamoDTO.Registrar.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(PrestamoDTO.Modificar.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(PrestamoDTO.CambiarEstado.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.changeStatus(dto);
    }
}