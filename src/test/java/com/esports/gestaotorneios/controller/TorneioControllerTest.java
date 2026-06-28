package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.Torneio;
import com.esports.gestaotorneios.service.TorneioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TorneioController.class)
@EnableMethodSecurity
public class TorneioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TorneioService torneioService;

    @Test
    @WithMockUser(roles = "ORGANIZADOR")
    public void devePermitirAcessoAFormularioDeTorneioParaOrganizador() throws Exception {
        mockMvc.perform(get("/torneios/novo"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CAPITAO")
    public void deveBloquearAcessoAFormularioDeTorneioParaCapitao() throws Exception {
        mockMvc.perform(get("/torneios/novo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ORGANIZADOR")
    public void devePermitirCriacaoDeTorneioParaOrganizador() throws Exception {
        mockMvc.perform(post("/torneios/salvar")
                        .with(csrf())
                        .param("nome", "Campeonato Brasileiro de LoL"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "CAPITAO")
    public void deveBloquearCriacaoDeTorneioParaCapitao() throws Exception {
        mockMvc.perform(post("/torneios/salvar")
                        .with(csrf())
                        .param("nome", "Campeonato Brasileiro de LoL"))
                .andExpect(status().isForbidden());
    }
}
