package br.edu.femass.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public abstract class Controller {

    @FXML
    void switchToEstoque(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.ESTOQUE);
    }

    @FXML
    void switchToNovaVenda(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.NOVA_VENDA);
    }

    @FXML
    void switchToNovaCompra(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.NOVA_COMPRA);
    }

    @FXML
    void switchToClientes(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.CLIENTES);
    }

    @FXML
    void switchToVendas(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.VENDAS);
    }

    @FXML
    void switchToCompras(ActionEvent event) {

    }

    @FXML
    void switchToFechamentos(ActionEvent event) {

    }

    @FXML
    void switchToFornecedores(ActionEvent event) {

    }


    @FXML
    void switchToNovoFechamento(ActionEvent event) {

    }

}
