package br.com.ctatitude.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Etapa implements Serializable {

    private int id;
    private List<ExercicioEtapa> exerciciosEtapa;
    private Date duracao;
    private Date descanso;
    private int round;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ExercicioEtapa> getExerciciosEtapa() {
        return exerciciosEtapa;
    }

    public void setExerciciosEtapa(List<ExercicioEtapa> exerciciosEtapa) {
        this.exerciciosEtapa = exerciciosEtapa;
    }

    public Date getDuracao() {
        return duracao;
    }

    public void setDuracao(Date duracao) {
        this.duracao = duracao;
    }

    public Date getDescanso() {
        return descanso;
    }

    public void setDescanso(Date descanso) {
        this.descanso = descanso;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
