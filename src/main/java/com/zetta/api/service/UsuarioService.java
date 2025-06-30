package com.zetta.api.service;

import com.zetta.api.dto.UsuarioDTO;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO toDTO(UsuarioModel model){
        return new UsuarioDTO(
                model.getId(),
                model.getNome(),
                model.getEmail(),
                model.getSenha(),
                model.getCargo()
        );
    }

    public UsuarioModel toEntity(UsuarioDTO dto){
        UsuarioModel model = new UsuarioModel();
        model.setId(dto.id());
        model.setNome(dto.nome());
        model.setEmail(dto.email());
        model.setSenha(dto.senha());
        model.setCargo(dto.cargo());
        return model;
    }

    public UsuarioDTO salvar(UsuarioDTO dto){
        UsuarioModel entity = toEntity(dto);
        if (usuarioRepository.findByEmail(entity.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        entity.setSenha(new BCryptPasswordEncoder().encode(entity.getSenha()));
        UsuarioModel salvo = usuarioRepository.save(entity);
        return toDTO(salvo);
    }
}
