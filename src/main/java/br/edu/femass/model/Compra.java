package br.edu.femass.model;

import lombok.Data;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class Compra {
    private long id;
    private LocalDate dataCompra;
    private double preco;
    private Map<Long, ProdutoOperacao> cameras = new HashMap<>();
    private int quantidade;
    private Fornecedor fornecedor;

    public void setCameras(Map<Long, ProdutoOperacao> cameras) {
        this.cameras = cameras;
        for(ProdutoOperacao produtoOperacao : cameras.values()){
            quantidade += produtoOperacao.getQuantidade();
            preco += produtoOperacao.getSubtotal();
        }
    }
    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        StringBuilder camerasVenda = new StringBuilder();
        for(ProdutoOperacao produtoOperacao : cameras.values()){
            camerasVenda.append(produtoOperacao.getCamera()).append(" - ").append(numberFormat.format(produtoOperacao.getValorUnitario()));
            camerasVenda.append(" - ").append(numberFormat.format(produtoOperacao.getSubtotal()));
            camerasVenda.append(" - ").append(produtoOperacao.getQuantidade()).append("un\n");
        }

        return "Compra " + id +
                "\nData: " + dataCompra +
                "\nQuantidade: " + quantidade +
                "\nTotal: " + numberFormat.format(preco) +
                "\nCliente: " + fornecedor.getNome() +
                "\n\n[Nome - Tipo - Marca - ValorUn - Subtotal - Quantidade]\n" + camerasVenda +
                "\nTotal: " + numberFormat.format(preco);
    }
}
