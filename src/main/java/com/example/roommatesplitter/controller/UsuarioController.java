package com.example.roommatesplitter.controller;

import com.example.roommatesplitter.dto.UsuarioLoginDTO;
import com.example.roommatesplitter.dto.UsuarioResponseDTO;
import com.example.roommatesplitter.dto.UsuarioSignupDTO;
import com.example.roommatesplitter.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/signup")
    public ResponseEntity<UsuarioResponseDTO> signup(@Valid @RequestBody UsuarioSignupDTO dto) {
        UsuarioResponseDTO usuarioCreado = usuarioService.registrarse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody UsuarioLoginDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.login(dto);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }
}