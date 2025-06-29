package com.zetta.api.dto;

import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;

public record TarefaDTO (
        Long id,
        String nome,
        String descricao,
        Prioridade prioridade,
        Status status,
        Long usuarioId
){}
