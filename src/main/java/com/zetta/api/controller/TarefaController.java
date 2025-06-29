package com.zetta.api.controller;

import com.zetta.api.dto.TarefaDTO;
import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.UsuarioRepository;
import com.zetta.api.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDTO> criar(@RequestBody TarefaDTO dto) {
        return ResponseEntity.ok(tarefaService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listar(Authentication auth) {
        UsuarioModel usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorUsuario(usuario));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TarefaDTO>> listarPorStatus(Authentication auth, @RequestParam Status status) {
        UsuarioModel usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorStatus(usuario, status));
    }

    @GetMapping("/prioridade/{prioridade}")
    public ResponseEntity<List<TarefaDTO>> listarPorStatus(Authentication auth, @RequestParam Prioridade prioridade) {
        UsuarioModel usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorPrioridade(usuario, prioridade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizar(@PathVariable Long id, @RequestBody TarefaDTO dto) {
        return ResponseEntity.ok(tarefaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
