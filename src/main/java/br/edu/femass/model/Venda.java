package br.edu.femass.model;

import br.edu.femass.dao.VendaDao;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class Venda {
    private long id;
    private LocalDate dataVenda;
    private int quantidade;
    private double preco;
    private Map<Long, ProdutoVenda> cameras = new HashMap<>();
    private Cliente cliente;

    public void addProdutoVenda(ProdutoVenda produtoVenda){
        cameras.put(produtoVenda.getCamera().getId(), produtoVenda);
        quantidade += produtoVenda.getQuantidade();
        preco += produtoVenda.getSubtotal();
    }

    public void setCameras(Map<Long, ProdutoVenda> cameras) {
        this.cameras = cameras;
        for(ProdutoVenda produtoVenda : cameras.values()){
            quantidade += produtoVenda.getQuantidade();
            preco += produtoVenda.getSubtotal();
        }
    }

    @Override
    public String toString() {
        return "Venda " + id +
                "\nData: " + dataVenda +
                "\nQuantidade: " + quantidade +
                "\nTotal: " + preco +
                "\nCliente: " + cliente.getNome() + " " + cliente.getSobrenome();
    }
}
