package br.com.ctatitude.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Classe Etapa
 */
public class Etapa implements Serializable {

    private Integer id;
    private List<ExercicioEtapa> exerciciosEtapa;
    private Integer duracao;
    private Integer descanso;
    private Integer round;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets exercicios etapa.
     *
     * @return the exercicios etapa
     */
    public List<ExercicioEtapa> getExerciciosEtapa() {
        return exerciciosEtapa;
    }

    /**
     * Sets exercicios etapa.
     *
     * @param exerciciosEtapa the exercicios etapa
     */
    public void setExerciciosEtapa(List<ExercicioEtapa> exerciciosEtapa) {
        this.exerciciosEtapa = exerciciosEtapa;
    }

    /**
     * Gets duracao.
     *
     * @return the duracao
     */
    public Integer getDuracao() {
        return duracao;
    }

    /**
     * Sets duracao.
     *
     * @param duracao the duracao
     */
    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    /**
     * Gets descanso.
     *
     * @return the descanso
     */
    public Integer getDescanso() {
        return descanso;
    }

    /**
     * Sets descanso.
     *
     * @param descanso the descanso
     */
    public void setDescanso(Integer descanso) {
        this.descanso = descanso;
    }

    /**
     * Gets round.
     *
     * @return the round
     */
    public Integer getRound() {
        return round;
    }

    /**
     * Sets round.
     *
     * @param round the round
     */
    public void setRound(Integer round) {
        this.round = round;
    }
}
