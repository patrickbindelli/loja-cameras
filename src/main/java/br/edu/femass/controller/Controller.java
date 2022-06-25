package br.edu.femass.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

public abstract class Controller {
    public void switchToEstoque(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.ESTOQUE);
    }
    public void switchToNovaVenda(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.NOVA_VENDA);
    }
    public void switchToNovaCompra(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.NOVA_COMPRA);
    }
    public void switchToClientes(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.CLIENTES);
    }

    public void switchToVendas(ActionEvent event) throws IOException {
        Utils.switchToScene(event, Scenes.VENDAS);
    }
}
