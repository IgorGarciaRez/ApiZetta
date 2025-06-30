package com.zetta.api.service;

import com.zetta.api.dto.UsuarioDTO;
import com.zetta.api.enums.Cargo;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@SpringBootTest
public class UsuarioServiceTest {
    @Autowired
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSalvarUsuarioSucesso(){
        UsuarioDTO dto = new UsuarioDTO(
                null, "Joao", "joao@email.com", "senha123", Cargo.USUARIO
        );
        Mockito.when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        UsuarioModel salvo = new UsuarioModel();
        salvo.setId(dto.id());
        salvo.setNome(dto.nome());
        salvo.setEmail(dto.email());
        salvo.setSenha("CODIFICADO");
        salvo.setCargo(dto.cargo());
        Mockito.when(usuarioRepository.save(Mockito.any())).thenReturn(salvo);
        UsuarioDTO resultado = usuarioService.salvar(dto);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Joao", resultado.nome());
        Assertions.assertEquals("joao@email.com", resultado.email());
    }

    @Test
    public void testSalvarUsuarioDuplicado(){
        UsuarioDTO dto = new UsuarioDTO(
                null, "Joao", "joao@email.com", "senha123", Cargo.USUARIO
        );
        UsuarioModel existente = new UsuarioModel();
        existente.setId(1L);
        existente.setEmail(dto.email());
        Mockito.when(usuarioRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(existente));
        Assertions.assertThrows(ResponseStatusException.class, () -> usuarioService.salvar(dto));
        Mockito.verify(usuarioRepository, Mockito.never()).save(Mockito.any());
    }
}