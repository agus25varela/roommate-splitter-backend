package com.example.roommatesplitter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoResponseDTO {

    private Long id;
    private String descripcion;
    private BigDecimal monto;
    private String quienPago;
    private LocalDate fecha;
    private Long usuarioId;

    // Constructores
    public GastoResponseDTO() {}

    public GastoResponseDTO(Long id, String descripcion, BigDecimal monto, String quienPago, LocalDate fecha, Long usuarioId) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.quienPago = quienPago;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
