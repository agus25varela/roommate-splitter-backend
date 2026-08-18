package com.example.roommatesplitter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")

public class Gasto { @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String descripcion;
    private BigDecimal monto;
    private String quienPago; // "yo", "roommate_a", "roommate_b"
    private LocalDate fecha;

    // Constructores
    public Gasto() {}

    public Gasto(String descripcion, BigDecimal monto, String quienPago, LocalDate fecha) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.quienPago = quienPago;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getQuienPago() {
        return quienPago;
    }

    public void setQuienPago(String quienPago) {
        this.quienPago = quienPago;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}
