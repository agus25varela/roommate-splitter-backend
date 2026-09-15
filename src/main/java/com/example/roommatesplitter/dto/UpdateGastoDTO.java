package com.example.roommatesplitter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateGastoDTO {

    private String descripcion;
    private BigDecimal monto;
    private String quienPago;
    private LocalDate fecha;

    // Constructores
    public UpdateGastoDTO() {}

    public UpdateGastoDTO(String descripcion, BigDecimal monto, String quienPago, LocalDate fecha) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.quienPago = quienPago;
        this.fecha = fecha;
    }

    // Getters y Setters
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getQuienPago() { return quienPago; }
    public void setQuienPago(String quienPago) { this.quienPago = quienPago; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}