package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import com.sibi.GestionDeBibliotecas.Inventario.Model.Inventario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "loan")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long prestamoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventario inventario;

    @Column(name = "loan_date", nullable = false)
    private java.util.Date fechaPrestamo;

    @Column(name = "due_date", nullable = false)
    private java.util.Date fechaVencimiento;

    @Column(name = "return_date")
    private java.util.Date fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Estado estado = Estado.PENDIENTE;

    public enum Estado {
        PENDIENTE,
        DEVUELTO
    }
}
