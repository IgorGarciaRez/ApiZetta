package com.zetta.api.controller;

import com.zetta.api.dto.UsuarioDTO;
import com.zetta.api.enums.Cargo;
import com.zetta.api.service.JwtService;
import com.zetta.api.service.UsuarioDetailsService;
import com.zetta.api.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UsuarioController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @MockitoBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void testRegistrarUsuario() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "joao", "joao@email.com", "123", Cargo.USUARIO);
        UsuarioDTO salvo = new UsuarioDTO(1L, "joao", "joao@email.com", "criptografada", Cargo.USUARIO);

        Mockito.when(usuarioService.salvar(Mockito.any())).thenReturn(salvo);

        mockMvc.perform(post("/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "joao",
                                    "email": "joao@email.com",
                                    "senha": "123",
                                    "cargo": "USUARIO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }
}
