package br.edu.femass.model;

import lombok.Data;

@Data
public class Cliente {
    private long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;

    public String getNomeCompleto(){
        return nome + " " + sobrenome;
    }
}
