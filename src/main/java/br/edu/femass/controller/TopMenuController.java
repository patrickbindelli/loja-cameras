package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public abstract class TopMenuController {

    @FXML
    void switchToEstoque(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.ESTOQUE);
    }

    @FXML
    void switchToNovaVenda(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.NOVA_VENDA);
    }

    @FXML
    void switchToNovaCompra(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.NOVA_COMPRA);
    }

    @FXML
    void switchToClientes(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.CLIENTES);
    }

    @FXML
    void switchToVendas(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.VENDAS);
    }

    @FXML
    void switchToCompras(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.COMPRAS);
    }

    @FXML
    void switchToFornecedores(ActionEvent event) throws IOException {
        ControllerCommons.switchToScene(event, ScenesEnum.FORNECEDORES);
    }

    @FXML
    void switchToNovoFechamento() throws IOException {
        FechamentoController.display();
    }

}
