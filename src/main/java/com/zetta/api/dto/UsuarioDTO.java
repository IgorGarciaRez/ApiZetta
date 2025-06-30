package com.zetta.api.dto;

import com.zetta.api.enums.Cargo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de um novo usuário")
public record UsuarioDTO (
        @Schema(description = "ID único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Nome do usuário", example = "joao.silva")
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @Schema(description = "Email do usuário", example = "joao.silva@gmail.com")
        @NotBlank(message = "O email não pode estar em branco")
        String email,

        @Schema(description = "Senha do usuário", example = "Senha1234")
        @NotBlank(message = "A senha não pode estar em branco")
        String senha,

        @Schema(description = "Cargo do usuário", example = "ADMIN", allowableValues = {"ADMIN", "USUARIO", "LEITOR"})
        @NotNull(message = "O cargo não pode ser nulo")
        Cargo cargo
) {}
