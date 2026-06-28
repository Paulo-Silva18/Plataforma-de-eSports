package com.esports.gestaotorneios.integration;

import com.esports.gestaotorneios.model.Equipe;
import com.esports.gestaotorneios.model.Jogador;
import com.esports.gestaotorneios.repository.EquipeRepository;
import com.esports.gestaotorneios.repository.JogadorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Garante que as mudanças no banco sejam desfeitas ao final do teste
public class FluxoCompletoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Test
    @WithMockUser(roles = "CAPITAO")
    public void deveCriarEquipeEAdicionarJogadorComSucesso() throws Exception {
        // 1. Simular POST para salvar uma nova Equipe
        mockMvc.perform(post("/equipes/salvar")
                        .with(csrf())
                        .param("nome", "PaiN Gaming")
                        .param("tag", "PNG")
                        .param("dataFundacao", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipes"));

        // Verificar o estado real do Banco de Dados usando o Repository
        assertFalse(equipeRepository.findAll().isEmpty(), "A equipe não foi salva no banco de dados.");
        Equipe equipeSalva = equipeRepository.findAll().stream()
                .filter(e -> "PaiN Gaming".equals(e.getNome()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Equipe 'PaiN Gaming' não encontrada"));
        assertEquals("PaiN Gaming", equipeSalva.getNome());
        assertEquals("PNG", equipeSalva.getTag());

        // 2. Simular POST para salvar um novo Jogador vinculado a essa equipe
        mockMvc.perform(post("/jogadores/salvar")
                        .with(csrf())
                        .param("nome", "Felipe Gonçalves")
                        .param("nickname", "brTT")
                        .param("posicao", "Atirador") // 'posicao', não 'funcao' baseado no modelo
                        .param("equipe.id", equipeSalva.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jogadores"));

        // Verificar o estado real do Banco de Dados para Jogador
        assertFalse(jogadorRepository.findAll().isEmpty(), "O jogador não foi salvo no banco de dados.");
        Jogador jogadorSalvo = jogadorRepository.findAll().stream()
                .filter(j -> "brTT".equals(j.getNickname()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Jogador 'brTT' não encontrado"));
        assertEquals("brTT", jogadorSalvo.getNickname());
        
        // Comprovar o relacionamento das tabelas (Integração concluída com sucesso!)
        assertEquals(equipeSalva.getId(), jogadorSalvo.getEquipe().getId());
    }
}
