package com.zetta.api.controller;

import com.zetta.api.dto.TarefaDTO;
import com.zetta.api.enums.Cargo;
import com.zetta.api.enums.Prioridade;
import com.zetta.api.enums.Status;
import com.zetta.api.model.UsuarioModel;
import com.zetta.api.repository.UsuarioRepository;
import com.zetta.api.service.TarefaService;
import com.zetta.api.component.JwtAuthFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    private UsuarioModel usuario;

    @BeforeEach
    void setup() {
        usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setEmail("user@email.com");
        usuario.setCargo(Cargo.USUARIO);
    }

    @Test
    @WithMockUser(username = "joao@email.com", roles = {"USUARIO"})
    void testCriarTarefa() throws Exception {
        TarefaDTO dto = new TarefaDTO(null, "Tarefa", "Descrição", Prioridade.MEDIA, Status.PENDENTE, 1L);
        TarefaDTO salvo = new TarefaDTO(10L, "Tarefa", "Descrição", Prioridade.MEDIA, Status.PENDENTE, 1L);

        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(usuario));
        Mockito.when(tarefaService.salvar(Mockito.any(), Mockito.any()))
                .thenReturn(salvo);

        mockMvc.perform(post("/tarefas")
                        .principal(() -> usuario.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "Tarefa",
                                    "descricao": "Descrição",
                                    "prioridade": "MEDIA",
                                    "status": "PENDENTE",
                                    "usuarioId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Tarefa"));
    }

    @Test
    @WithMockUser(username = "joao@email.com", roles = {"USUARIO"})
    void testListarTarefasDoUsuario() throws Exception {
        TarefaDTO dto = new TarefaDTO(1L, "Tarefa", "Desc", Prioridade.BAIXA, Status.CONCLUIDA, 1L);

        Mockito.when(usuarioRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.of(usuario));
        Mockito.when(tarefaService.listarPorUsuario(Mockito.eq(usuario), Mockito.any()))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/tarefas")
                        .principal(() -> usuario.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Tarefa"));
    }
}