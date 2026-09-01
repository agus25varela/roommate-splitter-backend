package com.example.roommatesplitter.controller;

import com.example.roommatesplitter.dto.GastoDTO;
import com.example.roommatesplitter.dto.GastoResponseDTO;
import com.example.roommatesplitter.dto.BalanceDTO;
import com.example.roommatesplitter.service.GastoService;
import com.example.roommatesplitter.service.DeudaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gasto")
@CrossOrigin(origins = "http://localhost:5173")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @Autowired
    private DeudaService deudaService;

    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> obtenerGasto(@RequestParam Long usuarioId) {
        List<GastoResponseDTO> gasto = gastoService.obtenerGastoPorUsuario(usuarioId);
        return ResponseEntity.ok(gasto);
    }

    @PostMapping
    public ResponseEntity<GastoResponseDTO> crearGasto(@Valid @RequestBody GastoDTO gastoDTO) {
        GastoResponseDTO gastoCreado = gastoService.crearGasto(gastoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(gastoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> obtenerGastoPorId(@PathVariable Long id) {
        GastoResponseDTO gasto = gastoService.obtenerGastoPorId(id);
        return ResponseEntity.ok(gasto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> actualizarGasto(
            @PathVariable Long id,
            @Valid @RequestBody GastoDTO gastoDTO) {
        GastoResponseDTO gastoActualizado = gastoService.actualizarGasto(id, gastoDTO);
        return ResponseEntity.ok(gastoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGasto(@PathVariable Long id) {
        gastoService.eliminarGasto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balances")
    public ResponseEntity<List<BalanceDTO>> obtenerBalances(@RequestParam Long usuarioId) {
        List<BalanceDTO> balances = deudaService.calcularBalances(usuarioId);
        return ResponseEntity.ok(balances);
    }
}
