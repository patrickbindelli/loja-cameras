package br.edu.femass.model;

import lombok.Data;

@Data
public class Fornecedor {
    private long id;
    private String nome;
    private String cnpj;
    private String telefone;
}
