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
    public ResponseEntity<TarefaDTO> criar(Authentication auth, @RequestBody TarefaDTO dto) {
        return ResponseEntity.ok(tarefaService.salvar(dto, auth));
    }

    /* ENDPOINTS PARA USUARIO ----------*/

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listar(Authentication auth) {
        UsuarioModel usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorUsuario(usuario, auth));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TarefaDTO>> listarPorStatus(Authentication auth, @RequestParam Status status) {
        UsuarioModel usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorStatus(auth, usuario.getId(), status));
    }

    @GetMapping("/prioridade/{prioridade}")
    public ResponseEntity<List<TarefaDTO>> listarPorStatus(Authentication auth, @RequestParam Prioridade prioridade) {
        UsuarioModel usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorPrioridade(auth, usuario.getId(), prioridade));
    }

    /* ENDPOINTS PARA ADMIN E LEITORES ----------*/

    @GetMapping("/status/{status}/{usuarioId}")
    public ResponseEntity<List<TarefaDTO>> listarStatusPorUsuario(Authentication auth, @PathVariable Status status, @PathVariable Long usuarioId) {
        return ResponseEntity.ok(tarefaService.listarPorStatus(auth, usuarioId, status));
    }

    @GetMapping("/prioridade/{prioridade}/{usuarioId}")
    public ResponseEntity<List<TarefaDTO>> listarPrioridadePorUsuario(Authentication auth, @PathVariable Prioridade prioridade, @PathVariable Long usuarioId) {
        return ResponseEntity.ok(tarefaService.listarPorPrioridade(auth, usuarioId, prioridade));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TarefaDTO>> listarPorUsuario(Authentication auth, @PathVariable Long usuarioId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        return ResponseEntity.ok(tarefaService.listarPorUsuario(usuario, auth));
    }

    /* ENDPOINTS PUT E DELETE ----------*/

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizar(Authentication auth, @PathVariable Long id, @RequestBody TarefaDTO dto) {
        return ResponseEntity.ok(tarefaService.atualizar(id, dto, auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(Authentication auth, @PathVariable Long id) {
        tarefaService.deletar(id, auth);
        return ResponseEntity.noContent().build();
    }
}
