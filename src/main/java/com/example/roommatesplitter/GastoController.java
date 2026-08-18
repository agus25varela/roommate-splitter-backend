package com.example.roommatesplitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/gastos")

public class GastoController {
    @Autowired
    private GastoRepository gastoRepository;

    @GetMapping
    public List<Gasto> getAllGastos() {
        return gastoRepository.findAll();
    }

    @PostMapping
    public Gasto createGasto(@RequestBody Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    @GetMapping("/balances")
    public List<BalanceDTO> getBalances() {
        List<Gasto> gastos = gastoRepository.findAll();

        // Calcular totales por persona
        double totalYo = 0;
        double totalRoommateA = 0;
        double totalRoommateB = 0;

        for (Gasto gasto : gastos) {
            if ("yo".equals(gasto.getQuienPago())) {
                totalYo += gasto.getMonto().doubleValue();
            } else if ("roommate_a".equals(gasto.getQuienPago())) {
                totalRoommateA += gasto.getMonto().doubleValue();
            } else if ("roommate_b".equals(gasto.getQuienPago())) {
                totalRoommateB += gasto.getMonto().doubleValue();
            }
        }

        // Calcular promedio
        double totalGastos = totalYo + totalRoommateA + totalRoommateB;
        double promedio = totalGastos / 3;

        // Calcular deudas
        List<BalanceDTO> balances = new ArrayList<>();

        double deudaA = promedio - totalRoommateA;
        double deudaB = promedio - totalRoommateB;

        if (deudaA > 0) {
            balances.add(new BalanceDTO("roommate_a", "yo", deudaA));
        }
        if (deudaB > 0) {
            balances.add(new BalanceDTO("roommate_b", "yo", deudaB));
        }

        return balances;
    }
}
