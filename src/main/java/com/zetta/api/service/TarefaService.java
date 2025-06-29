package com.zetta.api.service;

import com.zetta.api.dto.TarefaDTO;
import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import com.zetta.api.model.TarefaModel;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.TarefaRepository;
import com.zetta.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    public TarefaDTO toDTO(TarefaModel tarefa){
        return new TarefaDTO(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getDescricao(),
                tarefa.getPrioridade(),
                tarefa.getStatus(),
                tarefa.getUsuario().getId()
        );
    }

    public TarefaModel toEntity(TarefaDTO dto){
        UsuarioModel usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        TarefaModel tarefa = new TarefaModel();
        tarefa.setId(dto.id());
        tarefa.setNome(dto.nome());
        tarefa.setDescricao(dto.descricao());
        tarefa.setPrioridade(dto.prioridade());
        tarefa.setStatus(dto.status());
        tarefa.setUsuario(usuario);
        return tarefa;
    }

    public List<TarefaDTO> toDTOList(List<TarefaModel> lista) {
        return lista.stream().map(this::toDTO).toList();
    }

    public TarefaDTO salvar(TarefaDTO dto) {
        TarefaModel salvo = tarefaRepository.save(toEntity(dto));
        return toDTO(salvo);
    }

    public List<TarefaDTO> listarPorUsuario(UsuarioModel usuario) {
        return toDTOList(tarefaRepository.findAllByUsuarioId(usuario.getId()));
    }

    public List<TarefaDTO> listarPorStatus(UsuarioModel usuario, Status status) {
        return toDTOList(tarefaRepository.findAllByUsuarioIdAndStatus(usuario.getId(), status));
    }

    public List<TarefaDTO> listarPorPrioridade(UsuarioModel usuario, Prioridade prioridade) {
        return toDTOList(tarefaRepository.findAllByUsuarioIdAndPrioridade(usuario.getId(), prioridade));
    }

    public TarefaDTO atualizar(Long id, TarefaDTO dto) {
        TarefaModel existente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        existente.setNome(dto.nome());
        existente.setDescricao(dto.descricao());
        existente.setPrioridade(dto.prioridade());
        existente.setStatus(dto.status());

        return toDTO(tarefaRepository.save(existente));
    }

    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }
}
