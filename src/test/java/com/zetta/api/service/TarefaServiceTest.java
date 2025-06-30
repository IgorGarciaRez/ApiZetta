package com.zetta.api.service;

import com.zetta.api.dto.TarefaDTO;
import com.zetta.api.enums.Cargo;
import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import com.zetta.api.model.TarefaModel;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.TarefaRepository;
import com.zetta.api.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TarefaServiceTest {
    @Autowired
    private TarefaService tarefaService;

    @MockitoBean
    private TarefaRepository tarefaRepository;

    @MockitoBean
    UsuarioRepository usuarioRepository;

    @MockitoBean
    private Authentication auth;

    private UsuarioModel usuario;

    @BeforeEach
    public void setUp(){
        usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNome("Nome");
        usuario.setEmail("email");
        usuario.setSenha("senha");
        usuario.setCargo(Cargo.ADMIN);
        Mockito.when(auth.getName()).thenReturn(usuario.getEmail());
    }

    @Test
    public void testeSalvarTarefaSucesso(){
        TarefaDTO dto = new TarefaDTO(null, "Teste", "Descrição", Prioridade.MEDIA, Status.PENDENTE, usuario.getId());
        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.of(usuario));
        TarefaModel salvo = new TarefaModel();
        salvo.setId(10L);
        salvo.setNome(dto.nome());
        salvo.setDescricao(dto.descricao());
        salvo.setStatus(Status.PENDENTE);
        salvo.setPrioridade(dto.prioridade());
        salvo.setUsuario(usuario);

        Mockito.when(tarefaRepository.save(Mockito.any())).thenReturn(salvo);
        TarefaDTO resultado = tarefaService.salvar(dto, auth);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(10L, resultado.id());
        Assertions.assertEquals("Teste", resultado.nome());
    }

    @Test
    void testListarTarefasDoUsuario() {
        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));
        TarefaModel t1 = new TarefaModel();
        t1.setId(1L);
        t1.setNome("Tarefa 1");
        t1.setUsuario(usuario);

        Mockito.when(tarefaRepository.findAllByUsuarioId(usuario.getId()))
                .thenReturn(List.of(t1));
        List<TarefaDTO> resultado = tarefaService.listarPorUsuario(usuario, auth);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Tarefa 1", resultado.get(0).nome());
    }

    @Test
    void testAtualizarTarefa() {
        TarefaModel existente = new TarefaModel();
        existente.setId(1L);
        existente.setNome("Antiga");
        existente.setUsuario(usuario);

        TarefaDTO dto = new TarefaDTO(1L, "Nova", "Descrição atualizada", Prioridade.ALTA, Status.CONCLUIDA, usuario.getId());
        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));
        Mockito.when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        Mockito.when(tarefaRepository.save(Mockito.any()))
                .thenReturn(existente);

        TarefaDTO atualizado = tarefaService.atualizar(1L, dto, auth);
        Assertions.assertEquals("Nova", atualizado.nome());
        Assertions.assertEquals(Status.CONCLUIDA, atualizado.status());
    }

    @Test
    void testDeletarTarefa() {
        TarefaModel tarefa = new TarefaModel();
        tarefa.setId(1L);
        tarefa.setUsuario(usuario);

        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));
        Mockito.when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        Assertions.assertDoesNotThrow(() -> tarefaService.deletar(1L, auth));
        Mockito.verify(tarefaRepository, Mockito.times(1)).delete(tarefa);
    }
}
