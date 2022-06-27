package br.edu.femass.model;

public enum Tipo {
    SLR("SLR"),
    RANGEFINDER("Rangefinder"),
    POINTANDSHOOT("Point and Shoot"),
    DESCARTAVEL("Descartável"),
    TLR("TLR");

    private final String nome;

    Tipo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
