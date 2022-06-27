package br.edu.femass.model;

import lombok.Data;

@Data
public class ProdutoOperacao {
    private Camera camera;
    private Double valorUnitario;
    private Double subtotal;
    private Integer quantidade;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
        if(quantidade != null)
            this.setSubtotal(quantidade * valorUnitario);
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        if(valorUnitario != null)
            this.setSubtotal(quantidade * valorUnitario);
    }
}
