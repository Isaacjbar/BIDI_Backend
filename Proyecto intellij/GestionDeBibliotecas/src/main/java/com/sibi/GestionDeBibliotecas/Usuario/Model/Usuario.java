package com.sibi.GestionDeBibliotecas.Usuario.Model;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.Prestamo;
import com.sibi.GestionDeBibliotecas.Util.Estado;
import com.sibi.GestionDeBibliotecas.Util.Rol;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private Estado estado = Estado.ACTIVO;


    @Column(name = "phone_number", columnDefinition = "VARCHAR(15)")
    private String numeroTelefono;

    @Column(name = "code", columnDefinition = "VARCHAR(255)")
    private String codigo;

    @Column(name = "code_generated_at")
    private java.util.Date codigoGeneradoEn;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prestamo> prestamos;

    public Usuario() {

    }
    
    public Usuario(Long usuarioId, String nombre, String correo, String contrasena, Rol rol, Estado estado, String numeroTelefono, String codigo, Date codigoGeneradoEn) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.numeroTelefono = numeroTelefono;
        this.codigo = codigo;
        this.codigoGeneradoEn = codigoGeneradoEn;
    }

    public Usuario(String nombre, String correo, String contrasena, Rol rol, String numeroTelefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.numeroTelefono = numeroTelefono;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getCodigoGeneradoEn() {
        return codigoGeneradoEn;
    }

    public void setCodigoGeneradoEn(Date codigoGeneradoEn) {
        this.codigoGeneradoEn = codigoGeneradoEn;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}