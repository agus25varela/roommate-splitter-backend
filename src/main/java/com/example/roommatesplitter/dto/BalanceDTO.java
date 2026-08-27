package com.example.roommatesplitter.dto;

import java.math.BigDecimal;

public class BalanceDTO {

    private String deudor;
    private String acreedor;
    private BigDecimal monto;

    // Constructores
    public BalanceDTO() {}

    public BalanceDTO(String deudor, String acreedor, BigDecimal monto) {
        this.deudor = deudor;
        this.acreedor = acreedor;
        this.monto = monto;
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
}
