package com.sibi.GestionDeBibliotecas.Categoria.Controller;

import com.sibi.GestionDeBibliotecas.Categoria.Model.CategoriaDTO;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/status/{estado}")
    public ResponseEntity<Message> getByStatus(@PathVariable String estado) {
        return categoriaService.findByStatus(estado);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@Validated(CategoriaDTO.Registrar.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@Validated(CategoriaDTO.Modificar.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(CategoriaDTO.CambiarEstado.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.changeStatus(dto);
    }
}