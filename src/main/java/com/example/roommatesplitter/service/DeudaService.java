package com.example.roommatesplitter.service;

import com.example.roommatesplitter.dto.BalanceDTO;
import com.example.roommatesplitter.dto.DeudaDTO;
import com.example.roommatesplitter.model.Gasto;
import com.example.roommatesplitter.model.Deuda;
import com.example.roommatesplitter.model.Usuario;
import com.example.roommatesplitter.repository.GastoRepository;
import com.example.roommatesplitter.repository.DeudaRepository;
import com.example.roommatesplitter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DeudaService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private DeudaRepository deudaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Calcula los balances (quién debe cuánto) basado en los gastos registrados
     * Lógica: suma lo que pagó cada persona, calcula el promedio, calcula quién debe
     */
    @Transactional(readOnly = true)
    public List<BalanceDTO> calcularBalances(Long usuarioId) {
        validarQueUsuarioExiste(usuarioId);

        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuarioId);

        if (gastos.isEmpty()) {
            return new ArrayList<>();
        }

        // Calcular totales por persona
        Map<String, BigDecimal> totalPorPersona = new HashMap<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (Gasto gasto : gastos) {
            String quienPago = gasto.getQuienPago();
            BigDecimal monto = gasto.getMonto();

            totalPorPersona.put(quienPago, totalPorPersona.getOrDefault(quienPago, BigDecimal.ZERO).add(monto));
            totalGeneral = totalGeneral.add(monto);
        }

        // Calcular promedio (asumiendo 3 personas: yo, roommate_a, roommate_b)
        BigDecimal promedio = totalGeneral.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

        // Calcular deudas
        List<BalanceDTO> balances = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : totalPorPersona.entrySet()) {
            String persona = entry.getKey();
            BigDecimal totalPagado = entry.getValue();
            BigDecimal deuda = promedio.subtract(totalPagado);

            if (deuda.compareTo(BigDecimal.ZERO) > 0) {
                // Esta persona debe
                balances.add(new BalanceDTO(persona, "yo", deuda));
            } else if (deuda.compareTo(BigDecimal.ZERO) < 0) {
                // Esta persona tiene crédito (alguien le debe)
                balances.add(new BalanceDTO("yo", persona, deuda.abs()));
            }
        }

        return balances;
    }

    /**
     * Obtiene todas las deudas de un usuario
     */
    @Transactional(readOnly = true)
    public List<DeudaDTO> obtenerDeudasPorUsuario(Long usuarioId) {
        validarQueUsuarioExiste(usuarioId);

        List<Deuda> deudas = deudaRepository.findByUsuarioId(usuarioId);

        return deudas.stream()
                .map(this::convertirADeudaDTO)
                .toList();
    }

    /**
     * Marca una deuda como pagada
     */
    @Transactional
    public void marcarDeudaComoPagada(Long deudaId) {
        Deuda deuda = deudaRepository.findById(deudaId)
                .orElseThrow(() -> new IllegalArgumentException("Deuda con ID " + deudaId + " no encontrada"));

        deuda.setEstado("PAGADA");
        deudaRepository.save(deuda);
    }

    // Métodos privados

    private void validarQueUsuarioExiste(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException("Usuario con ID " + usuarioId + " no existe");
        }
    }

    private DeudaDTO convertirADeudaDTO(Deuda deuda) {
        return new DeudaDTO(
                deuda.getDeudor(),
                deuda.getAcreedor(),
                deuda.getMonto(),
                deuda.getEstado()
        );
    }
}
