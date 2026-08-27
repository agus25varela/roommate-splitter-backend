package com.example.roommatesplitter.controller;

import com.example.roommatesplitter.model.Usuario;
import com.example.roommatesplitter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuarioData) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioData.getEmail());

        if (usuarioExistente.isPresent()) {
            return usuarioExistente.get();
        } else {
            // Si no existe, crear nuevo usuario
            Usuario nuevoUsuario = new Usuario(usuarioData.getEmail(), usuarioData.getNombre());
            return usuarioRepository.save(nuevoUsuario);
        }
    }

    @PostMapping("/signup")
    public Usuario signup(@RequestBody Usuario usuarioData) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioData.getEmail());

        if (usuarioExistente.isPresent()) {
            return usuarioExistente.get();
        }

        Usuario nuevoUsuario = new Usuario(usuarioData.getEmail(), usuarioData.getNombre());
        return usuarioRepository.save(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public Usuario getUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
