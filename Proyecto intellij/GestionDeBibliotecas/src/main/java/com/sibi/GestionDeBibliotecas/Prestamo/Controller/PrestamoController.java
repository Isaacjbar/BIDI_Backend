package com.sibi.GestionDeBibliotecas.Prestamo.Controller;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<Message> getAllByUsuario(@PathVariable Long userId) {
        return prestamoService.findAllByUsuario(userId);
    }

    @GetMapping("/status/{estado}")
    public ResponseEntity<Message> getByStatus(@PathVariable String estado) {
        return prestamoService.findByStatus(estado);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(PrestamoDTO.Registrar.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.save(dto);
    }
    @PostMapping("/save/for-customer")
    public ResponseEntity<Message> saveForCustomer(@RequestBody PrestamoDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);  // Eliminar "Bearer "
        return prestamoService.saveForCustomer(dto, token);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(PrestamoDTO.CambiarEstado.class) @RequestBody PrestamoDTO dto) {
        return prestamoService.changeStatus(dto);
    }
}