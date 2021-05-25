package br.com.ctatitude.model;

import java.io.Serializable;

/**
 * Classe Exercicio
 */
public class Exercicio implements Serializable {

    private Integer id;
    private String nome;
    private String descricao;
    private String repeticoes;
    private String peso;
    private String distancia;
    private String tempo;

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
     * Gets descricao.
     *
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Sets descricao.
     *
     * @param descricao the descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Gets repeticoes.
     *
     * @return the repeticoes
     */
    public String getRepeticoes() {
        return repeticoes;
    }

    /**
     * Sets repeticoes.
     *
     * @param repeticoes the repeticoes
     */
    public void setRepeticoes(String repeticoes) {
        this.repeticoes = repeticoes;
    }

    /**
     * Gets peso.
     *
     * @return the peso
     */
    public String getPeso() {
        return peso;
    }

    /**
     * Sets peso.
     *
     * @param peso the peso
     */
    public void setPeso(String peso) {
        this.peso = peso;
    }

    /**
     * Gets distancia.
     *
     * @return the distancia
     */
    public String getDistancia() {
        return distancia;
    }

    /**
     * Sets distancia.
     *
     * @param distancia the distancia
     */
    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    /**
     * Gets tempo.
     *
     * @return the tempo
     */
    public String getTempo() {
        return tempo;
    }

    /**
     * Sets tempo.
     *
     * @param tempo the tempo
     */
    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
