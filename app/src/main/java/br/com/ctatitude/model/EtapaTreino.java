package br.com.ctatitude.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EtapaTreino implements Serializable {

    private int id;
    private Etapa etapa;
    private int ordem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
}
