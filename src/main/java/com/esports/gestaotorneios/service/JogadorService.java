package com.esports.gestaotorneios.service;

import com.esports.gestaotorneios.model.Equipe;
import com.esports.gestaotorneios.model.Jogador;
import com.esports.gestaotorneios.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final EquipeService equipeService;

    // Injeção de dependência do Repository e da Service de Equipes
    public JogadorService(JogadorRepository jogadorRepository, EquipeService equipeService) {
        this.jogadorRepository = jogadorRepository;
        this.equipeService = equipeService;
    }

    @Transactional(readOnly = true)
    public List<Jogador> findAll() {
        return jogadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Jogador findById(Long id) {
        return jogadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado com ID: " + id));
    }

    @Transactional
    public Jogador save(Jogador jogador) {
        validarJogador(jogador);
        
        // Regra de Negócio: Garante a vinculação correta com uma Equipe existente
        if (jogador.getEquipe() != null && jogador.getEquipe().getId() != null) {
            // Busca a equipe no banco através da EquipeService para garantir que ela existe
            Equipe equipeExistente = equipeService.findById(jogador.getEquipe().getId());
            jogador.setEquipe(equipeExistente);
        } else {
            throw new IllegalArgumentException("O jogador precisa estar vinculado a uma equipe existente.");
        }

        return jogadorRepository.save(jogador);
    }

    @Transactional
    public void deleteById(Long id) {
        Jogador jogador = findById(id);
        jogadorRepository.delete(jogador);
    }

    private void validarJogador(Jogador jogador) {
        if (jogador.getNome() == null || jogador.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogador é obrigatório.");
        }

        if (jogador.getNickname() == null || jogador.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("O nickname do jogador é obrigatório.");
        }

        if (jogador.getPosicao() == null || jogador.getPosicao().trim().isEmpty()) {
            throw new IllegalArgumentException("A posição do jogador é obrigatória.");
        }

        // Regra de Negócio: Validação de Nickname Único (Nenhum jogador pode ter o mesmo nick)
        Optional<Jogador> jogadorExistente = jogadorRepository.findByNickname(jogador.getNickname());
        if (jogadorExistente.isPresent() && !jogadorExistente.get().getId().equals(jogador.getId())) {
            throw new IllegalArgumentException("Já existe um jogador cadastrado com o nickname: " + jogador.getNickname());
        }
    }
}
