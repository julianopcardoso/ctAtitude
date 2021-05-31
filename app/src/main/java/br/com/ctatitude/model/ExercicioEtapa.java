package br.com.ctatitude.model;

import java.io.Serializable;

/**
 * Classe ExercicioEtapa
 */
public class ExercicioEtapa implements Serializable {
    private Integer idExercicio;
    private Integer idEtapa;
    private Integer ordem;
    private Exercicio exercicio;
    private Integer pesoMasc;
     private Integer pesoFem;
    private Integer repeticao;
    private Integer distancia;
    private Integer tempo;

    /**
     * Gets id exercicio.
     *
     * @return the id exercicio
     */
    public Integer getIdExercicio() {
        return idExercicio;
    }

    /**
     * Sets id exercicio.
     *
     * @param idExercicio the id exercicio
     */
    public void setIdExercicio(Integer idExercicio) {
        this.idExercicio = idExercicio;
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
     * Gets exercicio.
     *
     * @return the exercicio
     */
    public Exercicio getExercicio() {
        return exercicio;
    }

    /**
     * Sets exercicio.
     *
     * @param exercicio the exercicio
     */
    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }

    /**
     * Gets peso masc.
     *
     * @return the peso masc
     */
    public Integer getPesoMasc() {
        return pesoMasc;
    }

    /**
     * Sets peso masc.
     *
     * @param peso the peso
     */
    public void setPesoMasc(Integer peso) {
        this.pesoMasc = peso;
    }

    /**
     * Gets peso fem.
     *
     * @return the peso fem
     */
    public Integer getPesoFem() {
        return pesoFem;
    }

    /**
     * Sets peso fem.
     *
     * @param pesoFem the peso fem
     */
    public void setPesoFem(Integer pesoFem) {
        this.pesoFem = pesoFem;
    }

    /**
     * Gets repeticao.
     *
     * @return the repeticao
     */
    public Integer getRepeticao() {
        return repeticao;
    }

    /**
     * Sets repeticao.
     *
     * @param repeticao the repeticao
     */
    public void setRepeticao(Integer repeticao) {
        this.repeticao = repeticao;
    }

    /**
     * Gets distancia.
     *
     * @return the distancia
     */
    public Integer getDistancia() {
        return distancia;
    }

    /**
     * Sets distancia.
     *
     * @param distancia the distancia
     */
    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    /**
     * Gets tempo.
     *
     * @return the tempo
     */
    public Integer getTempo() {
        return tempo;
    }

    /**
     * Sets tempo.
     *
     * @param tempo the tempo
     */
    public void setTempo(Integer tempo) {
        this.tempo = tempo;
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
