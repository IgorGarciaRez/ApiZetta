package com.zetta.api.dto;

import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de uma tarefa")
public record TarefaDTO (
        @Schema(description = "ID único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Nome da tarefa", example = "Terminar atividade 1")
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @Schema(description = "Descricao da tarefa", example = "Atividade passada pelo professor Joao")
        String descricao,

        @Schema(description = "Prioridade da tarefa", example = "MEDIA", allowableValues = {"BAIXA", "MEDIA", "ALTA"})
        @NotNull(message = "O cargo não pode ser nulo")
        Prioridade prioridade,

        @Schema(description = "Status da tarefa", example = "PENDENTE", allowableValues = {"PENDENTE", "CONCLUIDA"})
        @NotNull(message = "O status não pode ser nulo")
        Status status,

        @Schema(description = "Id do dono da tarefa", example = "1")
        @NotBlank(message = "O id não pode estar em branco")
        Long usuarioId
){}
