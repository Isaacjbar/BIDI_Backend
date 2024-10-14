package com.sibi.GestionDeBibliotecas.Libro.Controller;

import com.sibi.GestionDeBibliotecas.Libro.Model.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LibroService {
    private static final Logger logger = LoggerFactory.getLogger(LibroService.class);
    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }


}