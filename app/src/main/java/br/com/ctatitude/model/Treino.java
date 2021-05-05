package br.com.ctatitude.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Treino implements Serializable {

    private int id;
    private String nome;
    private int round;
    private Date descanso;
    private List<EtapaTreino> etapasTreinos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Date getDescanso() {
        return descanso;
    }

    public void setDescanso(Date descanso) {
        this.descanso = descanso;
    }

    public List<EtapaTreino> getEtapasTreinos() {
        return etapasTreinos;
    }

    public void setEtapasTreinos(List<EtapaTreino> etapasTreinos) {
        this.etapasTreinos = etapasTreinos;
    }
}
