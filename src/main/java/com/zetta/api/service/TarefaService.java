package com.zetta.api.service;

import com.zetta.api.dto.TarefaDTO;
import com.zetta.api.dto.UsuarioDTO;
import com.zetta.api.enums.Cargo;
import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import com.zetta.api.model.TarefaModel;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.TarefaRepository;
import com.zetta.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public TarefaDTO salvar(TarefaDTO dto, Authentication auth) {
        UsuarioModel atual = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if(atual.getCargo() == Cargo.ADMIN || atual.getId().equals(dto.usuarioId())){
            TarefaModel salvo = tarefaRepository.save(toEntity(dto));
            return toDTO(salvo);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para criar tarefa; ");
    }

    public List<TarefaDTO> listarPorUsuario(UsuarioModel usuario, Authentication auth) {
        UsuarioModel atual = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow();
        if(atual.getCargo() == Cargo.ADMIN || atual.getCargo() == Cargo.LEITOR || atual.getId().equals(usuario.getId())){
            return toDTOList(tarefaRepository.findAllByUsuarioId(usuario.getId()));
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissao para ver as tarefas desse usuario; ");
    }

    public List<TarefaDTO> listarPorStatus(Authentication auth, Long usuarioId, Status status) {
        validarPermissaoLeitura(usuarioId, auth);
        return toDTOList(tarefaRepository.findAllByUsuarioIdAndStatus(usuarioId, status));
    }

    public List<TarefaDTO> listarPorPrioridade(Authentication auth, Long usuarioId, Prioridade prioridade) {
        validarPermissaoLeitura(usuarioId, auth);
        return toDTOList(tarefaRepository.findAllByUsuarioIdAndPrioridade(usuarioId, prioridade));
    }

    public TarefaDTO atualizar(Long id, TarefaDTO dto, Authentication auth) {
        UsuarioModel atual = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        TarefaModel existente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if(atual.getCargo() == Cargo.ADMIN || atual.getId().equals(dto.id())){
            existente.setNome(dto.nome());
            existente.setDescricao(dto.descricao());
            existente.setPrioridade(dto.prioridade());
            existente.setStatus(dto.status());
            return toDTO(tarefaRepository.save(existente));
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissao para editar tarefa; ");
    }

    public void deletar(Long id, Authentication auth) {
        UsuarioModel atual = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        TarefaModel tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa nao encontrada; "));
        if(atual.getCargo() == Cargo.ADMIN || atual.getId().equals(tarefa.getUsuario().getId())) {
            tarefaRepository.delete(tarefa);
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissao para excluir tarefa; ");
        }
    }

    private void validarPermissaoLeitura(Long usuarioId, Authentication auth) {
        UsuarioModel atual = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow();

        if (!(atual.getCargo() == Cargo.ADMIN || atual.getCargo() == Cargo.LEITOR || atual.getId().equals(usuarioId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
    }
}
