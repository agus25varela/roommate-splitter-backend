package com.example.roommatesplitter.service;

import com.example.roommatesplitter.dto.GastoDTO;
import com.example.roommatesplitter.dto.GastoResponseDTO;
import com.example.roommatesplitter.model.Gasto;
import com.example.roommatesplitter.model.Usuario;
import com.example.roommatesplitter.repository.GastoRepository;
import com.example.roommatesplitter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        validarQueUsuarioExiste(usuarioId);
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
                .orElseThrow(() -> new IllegalArgumentException("Usuario con ID " + gastoDTO.getUsuarioId() + " no encontrado"));

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
     * Actualiza un gasto existente
     */
    @Transactional
    public GastoResponseDTO actualizarGasto(Long id, GastoDTO gastoDTO) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gasto con ID " + id + " no encontrado"));

        validarQueUsuarioExiste(gastoDTO.getUsuarioId());

        gasto.setDescripcion(gastoDTO.getDescripcion());
        gasto.setMonto(gastoDTO.getMonto());
        gasto.setQuienPago(gastoDTO.getQuienPago());
        gasto.setFecha(gastoDTO.getFecha());

        Gasto gastoActualizado = gastoRepository.save(gasto);
        return convertirAResponseDTO(gastoActualizado);
    }

    /**
     * Elimina un gasto
     */
    @Transactional
    public void eliminarGasto(Long id) {
        if (!gastoRepository.existsById(id)) {
            throw new IllegalArgumentException("Gasto con ID " + id + " no encontrado");
        }
        gastoRepository.deleteById(id);
    }

    /**
     * Obtiene un gasto específico
     */
    @Transactional(readOnly = true)
    public GastoResponseDTO obtenerGastoPorId(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gasto con ID " + id + " no encontrado"));
        return convertirAResponseDTO(gasto);
    }

    // Métodos privados de utilidad

    private void validarQueUsuarioExiste(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException("Usuario con ID " + usuarioId + " no existe");
        }
    }

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
