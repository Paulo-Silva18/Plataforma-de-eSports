package com.esports.gestaotorneios.service;

import com.esports.gestaotorneios.model.Equipe;
import com.esports.gestaotorneios.model.Torneio;
import com.esports.gestaotorneios.repository.TorneioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TorneioService {

    private final TorneioRepository torneioRepository;
    private final EquipeService equipeService;

    // Injeção de dependência da Service de Equipes
    public TorneioService(TorneioRepository torneioRepository, EquipeService equipeService) {
        this.torneioRepository = torneioRepository;
        this.equipeService = equipeService;
    }

    @Transactional(readOnly = true)
    public List<Torneio> findAll() {
        return torneioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Torneio findById(Long id) {
        return torneioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Torneio não encontrado com ID: " + id));
    }

    @Transactional
    public Torneio save(Torneio torneio) {
        validarTorneio(torneio);
        return torneioRepository.save(torneio);
    }

    @Transactional
    public void deleteById(Long id) {
        Torneio torneio = findById(id);
        torneioRepository.delete(torneio);
    }

    // --- LÓGICA DE NEGÓCIO: Adicionar e Remover Equipes ---

    @Transactional
    public void adicionarEquipe(Long torneioId, Long equipeId) {
        Torneio torneio = findById(torneioId);
        Equipe equipe = equipeService.findById(equipeId);

        // Verifica de forma segura se a equipe já existe no torneio comparando os IDs
        boolean equipeJaCadastrada = torneio.getEquipes().stream()
                .anyMatch(e -> e.getId().equals(equipeId));

        if (equipeJaCadastrada) {
            throw new IllegalArgumentException("Esta equipe já está participando deste torneio.");
        }

        torneio.getEquipes().add(equipe);
        torneioRepository.save(torneio);
    }

    @Transactional
    public void removerEquipe(Long torneioId, Long equipeId) {
        Torneio torneio = findById(torneioId);
        
        // Garante que a equipe existe no banco antes de tentar remover
        equipeService.findById(equipeId); 

        boolean removida = torneio.getEquipes().removeIf(e -> e.getId().equals(equipeId));

        if (!removida) {
             throw new IllegalArgumentException("Esta equipe não está participando deste torneio.");
        }

        torneioRepository.save(torneio);
    }

    private void validarTorneio(Torneio torneio) {
        if (torneio.getNome() == null || torneio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do torneio é obrigatório.");
        }

        if (torneio.getPremiacao() != null && torneio.getPremiacao() < 0) {
            throw new IllegalArgumentException("A premiação não pode ser um valor negativo.");
        }

        if (torneio.getDataInicio() == null) {
            throw new IllegalArgumentException("A data de início do torneio é obrigatória.");
        }

        if (torneio.getDataFim() == null) {
            throw new IllegalArgumentException("A data de término do torneio é obrigatória.");
        }

        // Regra de Negócio Crítica: A data final não pode ser antes da inicial
        if (torneio.getDataFim().isBefore(torneio.getDataInicio())) {
            throw new IllegalArgumentException("A data de término não pode ser anterior à data de início.");
        }
    }
}
