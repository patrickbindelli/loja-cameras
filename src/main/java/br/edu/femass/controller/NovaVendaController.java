package br.edu.femass.controller;

import br.edu.femass.dao.CameraDao;
import br.edu.femass.dao.ClienteDao;
import br.edu.femass.dao.VendaDao;
import br.edu.femass.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class NovaVendaController extends Controller implements Initializable {

    private final Map<Long, ProdutoVenda> mapVendas = new HashMap<>();

    @FXML
    private Button btnCancelar, btnClose, btnFinalizar, btnRemover, btnBuscar;

    @FXML
    private TextField txtId, txtPreco, txtQuantidade, txtTotal;

    @FXML
    private TableView<ProdutoVenda> tbNovaVenda;

    @FXML
    private TableColumn<ProdutoVenda, String> colCodgo;

    @FXML
    private TableColumn<ProdutoVenda, String> colDesc;

    @FXML
    private TableColumn<ProdutoVenda, String> colMarca;

    @FXML
    private TableColumn<ProdutoVenda, String> colNome;

    @FXML
    private TableColumn<ProdutoVenda, Integer> colQtd;

    @FXML
    private TableColumn<ProdutoVenda, String> colTipo;

    @FXML
    private TableColumn<ProdutoVenda, Double> colTotal;

    @FXML
    private TableColumn<ProdutoVenda, String> colValor;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getId().toString()));
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getNome()));
        colDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getDescricao()));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getTipo().getNome()));
        colMarca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getMarca().toString()));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCamera().getPreco())));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    @FXML
    private void btnAdicionarAction(){
        try {
            Camera camera = new CameraDao().getById(Long.parseLong(txtId.getText()));
            ProdutoVenda produtoVenda = new ProdutoVenda();

            produtoVenda.setCamera(camera);
            produtoVenda.setQuantidade(Integer.valueOf(txtQuantidade.getText()));

            mapVendas.put(produtoVenda.getCamera().getId(), produtoVenda);

            carregarTbNovaVenda();
            calculaTotal();

            tbNovaVenda.getSelectionModel().select(produtoVenda);
            tbNovaVendaAction();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void btnFinalizarAction(ActionEvent event) {
        if (!mapVendas.isEmpty()) {
            try {

                Venda venda = new Venda();
                venda.setCameras(mapVendas);
                venda.setCliente(new ClienteDao().getById(12));
                venda.setDataVenda(LocalDate.now());
                System.out.println(venda);

                new VendaDao().create(venda);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Venda Finalizada");
                alert.setHeaderText("Informações da venda");
                alert.setContentText(venda.toString());
                alert.showAndWait();


                mapVendas.clear();
                carregarTbNovaVenda();
                calculaTotal();

                VendasController.setPreId(venda.getId());
                switchToVendas(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void carregarTbNovaVenda() {
        tbNovaVenda.setItems(FXCollections.observableArrayList(mapVendas.values()));
        tbNovaVenda.refresh();
    }

    private void calculaTotal(){
        Double total = 0d;
        for(ProdutoVenda produtoVenda : mapVendas.values()){
            total += produtoVenda.getSubtotal();
        }

        txtTotal.setText(total.toString());
    }

    @FXML
    private void btnRemoverAction(){
        if(tbNovaVenda.getSelectionModel().getSelectedItem() != null){
            ProdutoVenda produtoVenda = tbNovaVenda.getSelectionModel().getSelectedItem();
            mapVendas.remove(produtoVenda.getCamera().getId());

            calculaTotal();
            carregarTbNovaVenda();
        }
    }

    @FXML
    private void tbNovaVendaAction(){
        ProdutoVenda produtoVenda = tbNovaVenda.getSelectionModel().getSelectedItem();
        if(produtoVenda != null){
            txtId.setText(produtoVenda.getCamera().getId().toString());
            txtQuantidade.setText(produtoVenda.getQuantidade().toString());
            txtPreco.setText(String.valueOf(produtoVenda.getCamera().getPreco()));
        }
    }

    @FXML
    private void btnBuscarAction(ActionEvent event) throws IOException {
        EstoquePopUpController.setIdToSearch(Long.valueOf(txtId.getText()));
        Long searchedId = EstoquePopUpController.display();
        txtId.setText(searchedId.toString());
    }
}
