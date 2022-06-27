package br.edu.femass.model;

import lombok.Data;

@Data
public class Camera {
    private Long id;
    private String nome;
    private String descricao;
    private double preco;
    private int estoque;
    private Tipo tipo;
    private Marca marca;

    @Override
    public String toString() {
        return nome + " - " + tipo.getNome() + " - " + marca;
    }
}
