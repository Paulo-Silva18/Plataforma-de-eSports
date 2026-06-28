package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.Equipe;
import com.esports.gestaotorneios.service.EquipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipeController.class)
public class EquipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipeService equipeService;

    @Test
    @WithMockUser(roles = "CAPITAO")
    public void deveRetornarPaginaDeListagemDeEquipes() throws Exception {
        when(equipeService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/equipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipes/listar"))
                .andExpect(model().attributeExists("equipes"));
    }

    @Test
    @WithMockUser(roles = "CAPITAO")
    public void deveRetornarFormularioDeNovaEquipe() throws Exception {
        mockMvc.perform(get("/equipes/nova"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipes/form"))
                .andExpect(model().attributeExists("equipe"));
    }

    @Test
    @WithMockUser(roles = "CAPITAO")
    public void deveSalvarEquipeERedirecionar() throws Exception {
        mockMvc.perform(post("/equipes/salvar")
                        .with(csrf())
                        .param("nome", "LOUD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipes"));
    }
}
