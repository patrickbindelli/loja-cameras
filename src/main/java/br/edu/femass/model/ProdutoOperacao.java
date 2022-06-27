package br.edu.femass.model;

import lombok.Data;

@Data
public class ProdutoVenda {
    private Camera camera;
    private Double subtotal;
    private Integer quantidade;

    public void setCamera(Camera camera) {
        this.camera = camera;
        if(quantidade != null)
            this.setSubtotal(quantidade * camera.getPreco());
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        if(camera != null)
            this.setSubtotal(quantidade * camera.getPreco());
    }
}
