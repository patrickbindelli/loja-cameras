package br.edu.femass.model;

import lombok.Data;

public enum Tipo {
    SLR("SLR"),
    RANGEFINDER("Rangefinder"),
    POINTANDSHOOT("Point and Shoot"),
    DESCARTAVEL("Descartável"),
    TLR("TLR");

    private String nome;

    Tipo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
