package com.esports.gestaotorneios.service;

import com.esports.gestaotorneios.model.Equipe;
import com.esports.gestaotorneios.repository.EquipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Transactional(readOnly = true)
    public List<Equipe> findAll() {
        return equipeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Equipe findById(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada com ID: " + id));
    }

    @Transactional
    public Equipe save(Equipe equipe) {
        validarEquipe(equipe);
        return equipeRepository.save(equipe);
    }

    @Transactional
    public void deleteById(Long id) {
        Equipe equipe = findById(id);
        equipeRepository.delete(equipe);
    }

    private void validarEquipe(Equipe equipe) {
        if (equipe.getNome() == null || equipe.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da equipe é obrigatório.");
        }
        
        if (equipe.getTag() == null || equipe.getTag().trim().isEmpty()) {
            throw new IllegalArgumentException("A TAG da equipe é obrigatória.");
        }
        
        if (equipe.getTag().length() > 5) {
            throw new IllegalArgumentException("A TAG da equipe não pode ter mais de 5 caracteres.");
        }

        // Validação de regra de negócio: Nome único
        Optional<Equipe> equipeExistente = equipeRepository.findByNome(equipe.getNome());
        if (equipeExistente.isPresent() && !equipeExistente.get().getId().equals(equipe.getId())) {
            throw new IllegalArgumentException("Já existe uma equipe cadastrada com o nome: " + equipe.getNome());
        }
    }
}
