package br.edu.femass.controller;

import br.edu.femass.dao.CameraDao;
import br.edu.femass.model.Camera;
import br.edu.femass.model.Marca;
import br.edu.femass.model.Tipo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;

public class EstoqueController extends TopMenuController implements Initializable {

    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    @FXML
    private TableView<Camera> tbEstoque;

    @FXML
    private TableColumn<Camera, Long> colCodgo;

    @FXML
    private TableColumn<Camera, String> colDesc;

    @FXML
    private TableColumn<Camera, Marca> colMarca;

    @FXML
    private TableColumn<Camera, String> colNome;

    @FXML
    private TableColumn<Camera, Integer> colQtd;

    @FXML
    private TableColumn<Camera, Tipo> colTipo;

    @FXML
    private TableColumn<Camera, String> colValor;

    @FXML
    private TextField txtId, txtPreco, txtQuantidade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(numberFormat.format(cellData.getValue().getPreco())));

        carregaTabelaEstoque();
    }

    private void carregaTabelaEstoque() {
        try {
            List<Camera> cameraList = new CameraDao().read();
            ObservableList<Camera> observableListCameras = FXCollections.observableArrayList(cameraList);
            tbEstoque.setItems(observableListCameras);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void tbEstoqueOnClick(){
        if(tbEstoque.getSelectionModel().getSelectedItem() != null) {
            Camera camera = tbEstoque.getSelectionModel().getSelectedItem();
            txtId.setText(String.valueOf(camera.getId()));
            txtPreco.setText(String.valueOf(camera.getPreco()));
            txtQuantidade.setText(String.valueOf(camera.getEstoque()));
        }
    }

    @FXML
    private void btnBuscaAction() {
        tbEstoque.getItems().stream()
                .filter(item -> item.getId() == Integer.parseInt(txtId.getText()))
                .findAny()
                .ifPresent(item -> {
                    tbEstoque.getSelectionModel().select(item);
                    tbEstoque.scrollTo(item);
                    tbEstoqueOnClick();
                });
    }

    @FXML
    private void btnRemoverAction() throws Exception {
        if(tbEstoque.getSelectionModel().getSelectedItem() != null) {
            Camera camera = tbEstoque.getSelectionModel().getSelectedItem();
            new CameraDao().delete(camera);

            tbEstoque.getSelectionModel().clearSelection();
            txtId.setText("");
            txtPreco.setText("");
            txtQuantidade.setText("");
            carregaTabelaEstoque();
        }
    }

    @FXML
    private void btnAttPrecoAction(){
        if(tbEstoque.getSelectionModel().getSelectedItem() != null){
            Camera camera = tbEstoque.getSelectionModel().getSelectedItem();
            camera.setPreco(Double.parseDouble(txtPreco.getText()));

            tbEstoque.getSelectionModel().clearSelection();
            txtId.setText("");
            txtPreco.setText("");
            txtQuantidade.setText("");

            try {
                new CameraDao().update(camera);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            carregaTabelaEstoque();
            tbEstoque.getSelectionModel().select(camera);
            tbEstoque.scrollTo(camera);
            tbEstoqueOnClick();
        }
    }

    @FXML
    private void btnCadastrarAction() throws IOException {
        NovoProdutoController.display();
        carregaTabelaEstoque();
    }

    public void btnAtualizarAction(ActionEvent event) {
    }
}

