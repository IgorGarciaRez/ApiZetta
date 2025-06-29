package com.zetta.api.controller;

import com.zetta.api.enums.Status;
import com.zetta.api.model.TarefaModel;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.TarefaRepository;
import com.zetta.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<TarefaModel> criar(@RequestParam Long usuarioId, @RequestBody TarefaModel tarefa){
        return usuarioRepository.findById(usuarioId).map(usuario -> {
            tarefa.setUsuario(usuario);
            tarefa.setStatus(Status.PENDENTE);
            return ResponseEntity.ok(tarefaRepository.save(tarefa));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TarefaModel>> listarTodas(Authentication auth){
        String emailLogado = auth.getName();
        UsuarioModel usuarioLogado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado; "));
        return ResponseEntity.ok(tarefaRepository.findAllByUsuarioId(usuarioLogado.getId()));
    }

    @GetMapping("/status")
    public ResponseEntity<List<TarefaModel>> listarPorStatus(Authentication auth, @RequestParam Status status){
        String emailLogado = auth.getName();
        UsuarioModel usuarioLogado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado; "));
        return ResponseEntity.ok(tarefaRepository.findAllByUsuarioIdAndStatus(usuarioLogado.getId(), status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaModel> atualizar(@PathVariable Long tarefaId, @RequestBody TarefaModel novaTarefa){
        return tarefaRepository.findById(tarefaId).map(tarefa -> {
            tarefa.setNome(novaTarefa.getNome());
            tarefa.setDescricao(novaTarefa.getDescricao());
            tarefa.setStatus(novaTarefa.getStatus());
            return ResponseEntity.ok(tarefaRepository.save(tarefa));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TarefaModel> deletar(@PathVariable Long tarefaId){
        if(tarefaRepository.existsById(tarefaId)){
            tarefaRepository.deleteById(tarefaId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
