package br.edu.femass.controller;

public enum Scenes {
    ESTOQUE("estoque.fxml"),
    NOVA_VENDA("nova_venda.fxml"),
    NOVA_COMPRA("nova_compra.fxml"),
    NOVO_PRODUTO("novo_produto.fxml"),
    CLIENTES("clientes.fxml"),
    VENDAS("vendas.fxml"),
    ESTOQUE_POPUP("estoque_popup.fxml");
    private final String scene;
    Scenes(String scene) {
        this.scene = scene;
    }

    public String getScene(){
        return "/br/edu/femass/" + scene;
    }
}
