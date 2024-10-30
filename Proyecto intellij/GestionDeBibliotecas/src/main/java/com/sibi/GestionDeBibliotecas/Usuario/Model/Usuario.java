package com.sibi.GestionDeBibliotecas.Usuario.Model;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "user")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long usuarioId;

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    private String nombre;

    @Column(name = "email", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String correo;

    @Column(name = "password", columnDefinition = "VARCHAR(255)", nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Rol rol;

    public enum Rol {
        ADMINISTRADOR,
        CLIENTE,
        INVITADO
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Estado estado = Estado.ACTIVO;

    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    @Column(name = "phone_number", columnDefinition = "VARCHAR(15)")
    private String numeroTelefono;

    @Column(name = "code", columnDefinition = "VARCHAR(255)")
    private String codigo;

    @Column(name = "code_generated_at")
    private java.util.Date codigoGeneradoEn;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prestamo> prestamos;
}