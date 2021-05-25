package br.com.ctatitude.model;

import java.io.Serializable;

/**
 * Classe EtapaTreino
 */
public class EtapaTreino implements Serializable {

    private Integer idTreino;
    private Integer idEtapa;
    private Etapa etapa;
    private Integer ordem;

    /**
     * Gets id treino.
     *
     * @return the id treino
     */
    public Integer getIdTreino() {
        return idTreino;
    }

    /**
     * Sets id treino.
     *
     * @param idTreino the id treino
     */
    public void setIdTreino(Integer idTreino) {
        this.idTreino = idTreino;
    }

    /**
     * Gets etapa.
     *
     * @return the etapa
     */
    public Etapa getEtapa() {
        return etapa;
    }

    /**
     * Sets etapa.
     *
     * @param etapa the etapa
     */
    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    /**
     * Gets ordem.
     *
     * @return the ordem
     */
    public Integer getOrdem() {
        return ordem;
    }

    /**
     * Sets ordem.
     *
     * @param ordem the ordem
     */
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    /**
     * Gets id etapa.
     *
     * @return the id etapa
     */
    public Integer getIdEtapa() {
        return idEtapa;
    }

    /**
     * Sets id etapa.
     *
     * @param idEtapa the id etapa
     */
    public void setIdEtapa(Integer idEtapa) {
        this.idEtapa = idEtapa;
    }
}
