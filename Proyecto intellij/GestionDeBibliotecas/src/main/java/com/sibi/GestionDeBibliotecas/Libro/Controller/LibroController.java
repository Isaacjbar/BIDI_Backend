package com.sibi.GestionDeBibliotecas.Libro.Controller;

import com.sibi.GestionDeBibliotecas.Libro.Model.LibroDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class LibroController {

    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAll() {
        return libroService.findAll();
    }

    @GetMapping("/status/{estado}")
    public ResponseEntity<Message> getByStatus(@PathVariable String estado) {
        return libroService.findByStatus(estado);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(LibroDTO.Registrar.class) @RequestBody LibroDTO dto) {
        return libroService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(LibroDTO.Modificar.class) @RequestBody LibroDTO dto) {
        return libroService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(LibroDTO.CambiarEstado.class) @RequestBody LibroDTO dto) {
        return libroService.changeStatus(dto);
    }
}