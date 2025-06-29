package com.zetta.api.controller;

import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.UsuarioRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioModel> registrar(@RequestBody UsuarioModel usuario){
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().body(usuarioRepository.save(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioModel> login(@RequestBody UsuarioModel usuario){
        return usuarioRepository.findByEmail(usuario.getEmail())
                .filter(u -> u.getSenha().equals(usuario.getSenha()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
