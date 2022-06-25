package br.edu.femass.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class Compra {
    private long id;
    private LocalDate dataCompra;
    private double preco;
    private List<ProdutoCompra> cameras;
    private int quantidade;
    private Fornecedor fornecedor;
}
