package br.com.ctatitude.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Classe Treino
 */
public class Treino implements Serializable {

    private Integer id;
    private String nome;
    private Integer round;
    private Integer descanso;
    private Integer duracao;
    private List<EtapaTreino> etapasTreinos;


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
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * Gets etapas treinos.
     *
     * @return the etapas treinos
     */
    public List<EtapaTreino> getEtapasTreinos() {
        return etapasTreinos;
    }

    /**
     * Sets etapas treinos.
     *
     * @param etapasTreinos the etapas treinos
     */
    public void setEtapasTreinos(List<EtapaTreino> etapasTreinos) {
        this.etapasTreinos = etapasTreinos;
    }
}
