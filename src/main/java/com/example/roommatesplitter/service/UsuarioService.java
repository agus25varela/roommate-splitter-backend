package com.example.roommatesplitter.service;

import com.example.roommatesplitter.dto.UsuarioLoginDTO;
import com.example.roommatesplitter.dto.UsuarioResponseDTO;
import com.example.roommatesplitter.dto.UsuarioSignupDTO;
import com.example.roommatesplitter.model.Usuario;
import com.example.roommatesplitter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario con email, nombre y contraseña hasheada
     */
    public UsuarioResponseDTO registrarse(UsuarioSignupDTO dto) {
        // Validar que el email no exista
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear nuevo usuario con password hasheado
        Usuario nuevoUsuario = new Usuario(
                dto.getEmail(),
                dto.getNombre(),
                passwordEncoder.encode(dto.getPassword())
        );

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return convertirAResponseDTO(usuarioGuardado);
    }

    /**
     * Valida credenciales y retorna el usuario si son correctas
     */
    public UsuarioResponseDTO login(UsuarioLoginDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(dto.getEmail());

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        // Validar contraseña
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        return convertirAResponseDTO(usuario);
    }

    /**
     * Obtiene un usuario por ID
     */
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return convertirAResponseDTO(usuario);
    }

    /**
     * Convierte Usuario a UsuarioResponseDTO (sin exponer password)
     */
    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre()
        );
    }
}
