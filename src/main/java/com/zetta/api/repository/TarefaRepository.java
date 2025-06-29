package com.zetta.api.repository;

import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import com.zetta.api.model.TarefaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<TarefaModel,Long> {
    List<TarefaModel> findAllByUsuarioId(Long usuarioId);
    List<TarefaModel> findAllByUsuarioIdAndStatus(Long usuarioId, Status status);
    List<TarefaModel> findAllByUsuarioIdAndPrioridade(Long usuarioId, Prioridade prioridade);
}
