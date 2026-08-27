package com.example.roommatesplitter.dto;

import java.math.BigDecimal;

public class DeudaDTO {

    private String deudor;
    private String acreedor;
    private BigDecimal monto;
    private String estado;

    // Constructores
    public DeudaDTO() {}

    public DeudaDTO(String deudor, String acreedor, BigDecimal monto, String estado) {
        this.deudor = deudor;
        this.acreedor = acreedor;
        this.monto = monto;
        this.estado = estado;
    }

    // Getters y Setters
    public String getDeudor() {
        return deudor;
    }

    public void setDeudor(String deudor) {
        this.deudor = deudor;
    }

    public String getAcreedor() {
        return acreedor;
    }

    public void setAcreedor(String acreedor) {
        this.acreedor = acreedor;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
