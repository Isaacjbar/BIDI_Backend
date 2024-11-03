package com.sibi.GestionDeBibliotecas.Libro_Categoria.Controller;

import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
