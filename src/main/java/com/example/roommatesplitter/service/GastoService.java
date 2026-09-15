package com.example.roommatesplitter.service;

import com.example.roommatesplitter.dto.BalanceDTO;
import com.example.roommatesplitter.dto.GastoDTO;
import com.example.roommatesplitter.dto.GastoResponseDTO;
import com.example.roommatesplitter.dto.UpdateGastoDTO;
import com.example.roommatesplitter.exception.RecursoNoEncontradoException;
import com.example.roommatesplitter.model.Gasto;
import com.example.roommatesplitter.model.Usuario;
import com.example.roommatesplitter.repository.GastoRepository;
import com.example.roommatesplitter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los gastos de un usuario ordenados por fecha descendente
     */
    @Transactional(readOnly = true)
    public List<GastoResponseDTO> obtenerGastoPorUsuario(Long usuarioId) {
        // No validar existencia — si el usuario no existe, retorna lista vacía
        return gastoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo gasto
     */
    @Transactional
    public GastoResponseDTO crearGasto(GastoDTO gastoDTO) {
        Usuario usuario = usuarioRepository.findById(gastoDTO.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + gastoDTO.getUsuarioId() + " no encontrado"));

        Gasto gasto = new Gasto(
                gastoDTO.getDescripcion(),
                gastoDTO.getMonto(),
                gastoDTO.getQuienPago(),
                gastoDTO.getFecha(),
                usuario
        );

        Gasto gastoGuardado = gastoRepository.save(gasto);
        return convertirAResponseDTO(gastoGuardado);
    }

    /**
     * Actualiza un gasto existente (solo los campos no-nulos)
     */
    @Transactional
    public GastoResponseDTO actualizarGasto(Long id, UpdateGastoDTO gastoDTO) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Gasto con ID " + id + " no encontrado"));

        if (gastoDTO.getDescripcion() != null) {
            gasto.setDescripcion(gastoDTO.getDescripcion());
        }
        if (gastoDTO.getMonto() != null) {
            gasto.setMonto(gastoDTO.getMonto());
        }
        if (gastoDTO.getQuienPago() != null) {
            gasto.setQuienPago(gastoDTO.getQuienPago());
        }
        if (gastoDTO.getFecha() != null) {
            gasto.setFecha(gastoDTO.getFecha());
        }

        Gasto gastoActualizado = gastoRepository.save(gasto);
        return convertirAResponseDTO(gastoActualizado);
    }

    /**
     * Elimina un gasto
     */
    @Transactional
    public void eliminarGasto(Long id) {
        if (!gastoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Gasto con ID " + id + " no encontrado");
        }
        gastoRepository.deleteById(id);
    }

    /**
     * Obtiene un gasto específico
     */
    @Transactional(readOnly = true)
    public GastoResponseDTO obtenerGastoPorId(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Gasto con ID " + id + " no encontrado"));
        return convertirAResponseDTO(gasto);
    }

    /**
     * Calcula balances dinámicos: quién debe cuánto a quién.
     * Detecta automáticamente los participantes a partir de los gastos.
     */
    @Transactional(readOnly = true)
    public List<BalanceDTO> calcularBalances(Long usuarioId) {
        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioId);

        if (gastos.isEmpty()) {
            return new ArrayList<>();
        }

        // Totales por persona
        Map<String, BigDecimal> totalPorPersona = new HashMap<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (Gasto gasto : gastos) {
            String pagador = gasto.getQuienPago();
            BigDecimal monto = gasto.getMonto();
            totalPorPersona.merge(pagador, monto, BigDecimal::add);
            totalGeneral = totalGeneral.add(monto);
        }

        // Promedio dinámico entre los que participaron
        int numParticipantes = totalPorPersona.size();
        BigDecimal promedio = totalGeneral.divide(
                BigDecimal.valueOf(numParticipantes), 2, RoundingMode.HALF_UP);

        // Deuda neta de cada persona: positivo = acreedor, negativo = deudor
        Map<String, BigDecimal> neto = new HashMap<>();
        List<String> deudores = new ArrayList<>();
        List<String> acreedores = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : totalPorPersona.entrySet()) {
            BigDecimal saldo = entry.getValue().subtract(promedio);
            if (saldo.compareTo(BigDecimal.ZERO) < 0) {
                neto.put(entry.getKey(), saldo);
                deudores.add(entry.getKey());
            } else if (saldo.compareTo(BigDecimal.ZERO) > 0) {
                neto.put(entry.getKey(), saldo);
                acreedores.add(entry.getKey());
            }
        }

        // Minimizar transferencias: deudor con mayor deuda paga primero al acreedor con mayor crédito
        deudores.sort((a, b) -> neto.get(a).compareTo(neto.get(b)));
        acreedores.sort((a, b) -> neto.get(b).compareTo(neto.get(a)));

        List<BalanceDTO> balances = new ArrayList<>();
        int i = 0, j = 0;

        while (i < deudores.size() && j < acreedores.size()) {
            String deudor = deudores.get(i);
            String acreedor = acreedores.get(j);
            BigDecimal deudaDeudor = neto.get(deudor).abs();
            BigDecimal creditoAcreedor = neto.get(acreedor);

            BigDecimal monto = deudaDeudor.min(creditoAcreedor);

            if (monto.compareTo(BigDecimal.ZERO) > 0) {
                balances.add(new BalanceDTO(deudor, acreedor, monto));
            }

            neto.put(deudor, neto.get(deudor).add(monto));
            neto.put(acreedor, neto.get(acreedor).subtract(monto));

            if (neto.get(deudor).abs().compareTo(new BigDecimal("0.001")) < 0) {
                i++;
            }
            if (neto.get(acreedor).compareTo(new BigDecimal("0.001")) < 0) {
                j++;
            }
        }

        return balances;
    }

    // Métodos privados de utilidad

    private GastoResponseDTO convertirAResponseDTO(Gasto gasto) {
        return new GastoResponseDTO(
                gasto.getId(),
                gasto.getDescripcion(),
                gasto.getMonto(),
                gasto.getQuienPago(),
                gasto.getFecha(),
                gasto.getUsuario().getId()
        );
    }
}
