package br.edu.femass.model;

import lombok.Data;

@Data
public class Marca {
    private long id;
    private String nome;

    @Override
    public String toString() {
        return nome;
    }
}
