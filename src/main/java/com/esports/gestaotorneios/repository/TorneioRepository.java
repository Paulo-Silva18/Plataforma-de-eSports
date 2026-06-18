package com.esports.gestaotorneios.repository;

import com.esports.gestaotorneios.model.Torneio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneioRepository extends JpaRepository<Torneio, Long> {
}
