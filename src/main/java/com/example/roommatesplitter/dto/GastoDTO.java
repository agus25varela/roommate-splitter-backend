package com.example.roommatesplitter.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoDTO {

    @NotBlank(message = "Descripción no puede estar vacía")
    @Size(min = 3, max = 200, message = "Descripción debe tener entre 3 y 200 caracteres")
    private String descripcion;

    @NotNull(message = "Monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "Monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "Quién pagó no puede estar vacío")
    private String quienPago;

    @NotNull(message = "Fecha no puede ser nula")
    private LocalDate fecha;

    @NotNull(message = "Usuario ID no puede ser nulo")
    private Long usuarioId;

    // Constructores
    public GastoDTO() {}

    public GastoDTO(String descripcion, BigDecimal monto, String quienPago, LocalDate fecha, Long usuarioId) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.quienPago = quienPago;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
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
