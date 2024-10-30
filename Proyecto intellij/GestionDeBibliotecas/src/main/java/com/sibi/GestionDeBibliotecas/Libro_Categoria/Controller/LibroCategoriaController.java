package com.sibi.GestionDeBibliotecas.Libro_Categoria.Controller;

import com.sibi.GestionDeBibliotecas.Libro_Categoria.Model.LibroCategoriaDTO;
import com.sibi.GestionDeBibliotecas.Util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-category")
public class LibroCategoriaController {

    private final LibroCategoriaService libroCategoriaService;

    @Autowired
    public LibroCategoriaController(LibroCategoriaService libroCategoriaService) {
        this.libroCategoriaService = libroCategoriaService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAll() {
        return libroCategoriaService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(LibroCategoriaDTO.Registrar.class) @RequestBody LibroCategoriaDTO dto) {
        return libroCategoriaService.save(dto);
    }
}
