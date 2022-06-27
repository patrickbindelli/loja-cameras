package br.edu.femass.controller.Utils;

public enum ScenesEnum {
    ESTOQUE("estoque.fxml"),
    NOVA_VENDA("nova_venda.fxml"),
    NOVA_COMPRA("nova_compra.fxml"),
    NOVO_PRODUTO("novo_produto.fxml"),
    CLIENTES("clientes.fxml"),
    VENDAS("vendas.fxml"),
    COMPRAS("compras.fxml"),
    NOVO_CLIENTE("novo_cliente.fxml"),
    FORNECEDORES("fornecedores.fxml"),
    NOVO_FORNECEDOR("novo_fornecedor.fxml"),
    FORNECEDORES_POPUP("fornecedores_popup.fxml"),
    CLIENTES_POPUP("clientes_popup.fxml"),
    FECHAMENTO("fechamento.fxml"),
    ESTOQUE_POPUP("estoque_popup.fxml");
    private final String scene;
    ScenesEnum(String scene) {
        this.scene = scene;
    }

    public String getScene(){
        return "/br/edu/femass/" + scene;
    }
}
