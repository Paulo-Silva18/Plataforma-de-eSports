package com.esports.gestaotorneios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHorario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneio_id", nullable = false)
    private Torneio torneio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe1_id", nullable = false)
    private Equipe equipe1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe2_id", nullable = false)
    private Equipe equipe2;

    private Integer placarEquipe1 = 0;

    private Integer placarEquipe2 = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vencedor_id")
    private Equipe vencedor;

    public Partida() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHorario() {
        return dataHorario;
    }

    public void setDataHorario(LocalDateTime dataHorario) {
        this.dataHorario = dataHorario;
    }

    public Torneio getTorneio() {
        return torneio;
    }

    public void setTorneio(Torneio torneio) {
        this.torneio = torneio;
    }

    public Equipe getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(Equipe equipe1) {
        this.equipe1 = equipe1;
    }

    public Equipe getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(Equipe equipe2) {
        this.equipe2 = equipe2;
    }

    public Integer getPlacarEquipe1() {
        return placarEquipe1;
    }

    public void setPlacarEquipe1(Integer placarEquipe1) {
        this.placarEquipe1 = placarEquipe1;
    }

    public Integer getPlacarEquipe2() {
        return placarEquipe2;
    }

    public void setPlacarEquipe2(Integer placarEquipe2) {
        this.placarEquipe2 = placarEquipe2;
    }

    public Equipe getVencedor() {
        return vencedor;
    }

    public void setVencedor(Equipe vencedor) {
        this.vencedor = vencedor;
    }
}
