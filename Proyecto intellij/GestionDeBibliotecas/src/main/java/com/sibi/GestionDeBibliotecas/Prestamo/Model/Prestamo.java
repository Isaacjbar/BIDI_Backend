package com.sibi.GestionDeBibliotecas.Prestamo.Model;

import com.sibi.GestionDeBibliotecas.Inventario.Model.Inventario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "loan")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventario inventario;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date loanDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Status status = Status.PENDING;

    public enum Status {
        PENDING,
        RETURNED
    }

    public Prestamo() {

    }

    public Prestamo(Integer loanId, Usuario usuario, Inventario inventario, Date loanDate, Date dueDate, Date returnDate, Status status) {
        this.loanId = loanId;
        this.usuario = usuario;
        this.inventario = inventario;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Prestamo(Usuario usuario, Inventario inventario, Date loanDate, Date dueDate, Date returnDate, Status status) {
        this.usuario = usuario;
        this.inventario = inventario;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}