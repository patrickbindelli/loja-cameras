package br.edu.femass.controller;

import br.edu.femass.dao.*;
import br.edu.femass.model.Camera;
import br.edu.femass.model.Compra;
import br.edu.femass.model.ProdutoOperacao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.synedra.validatorfx.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class NovaCompraController extends TopMenuController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(NovaCompraController.class);
    private final Validator validator = new Validator();
    private final Map<Long, ProdutoOperacao> mapVendas = new HashMap<>();
    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    @FXML
    private Button btnAdicionar;

    @FXML
    private TextField txtId, txtPreco, txtQuantidade, txtTotal;

    @FXML
    private TableView<ProdutoOperacao> tbNovaCompra;

    @FXML
    private TableColumn<ProdutoOperacao, String> colCodgo;

    @FXML
    private TableColumn<ProdutoOperacao, String> colDesc;

    @FXML
    private TableColumn<ProdutoOperacao, String> colMarca;

    @FXML
    private TableColumn<ProdutoOperacao, String> colNome;

    @FXML
    private TableColumn<ProdutoOperacao, Integer> colQtd;

    @FXML
    private TableColumn<ProdutoOperacao, String> colTipo;

    @FXML
    private TableColumn<ProdutoOperacao, String> colTotal;

    @FXML
    private TableColumn<ProdutoOperacao, String> colValor;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getId().toString()));
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getNome()));
        colDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getDescricao()));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getTipo().getNome()));
        colMarca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamera().getMarca().toString()));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(numberFormat.format(cellData.getValue().getValorUnitario())));
        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(numberFormat.format(cellData.getValue().getSubtotal())));

        validator.createCheck()
                .dependsOn("id", txtId.textProperty())
                .dependsOn("quantidade", txtQuantidade.textProperty())
                .withMethod(c -> {
                    String error = "";
                    String id = c.get("id");
                    String quantidade = c.get("quantidade");
                    String regex = "\\d*";

                    if(!id.matches(regex)){
                        txtId.setText(id.replaceAll("[^\\d]", ""));
                    }

                    if(!quantidade.matches(regex)){
                        txtQuantidade.setText(id.replaceAll("[^\\d]", ""));
                    }

                    if(id.isEmpty()) error += "informe um código de produto.";
                    if(quantidade.isEmpty()) error += "\nInforme a quantidade a ser adicionada.";

                    btnAdicionar.setDisable(id.isEmpty() || quantidade.isEmpty());

                    if(!error.isEmpty()) c.error(error);
                })
                .decorates(btnAdicionar)
                .immediate();
    }

    @FXML
    private void btnAdicionarAction(){
        if(!txtId.getText().isEmpty()){
            try {
                Camera camera = new CameraDao().getById(Long.parseLong(txtId.getText()));
                ProdutoOperacao produtoOperacao = new ProdutoOperacao();

                produtoOperacao.setCamera(camera);
                produtoOperacao.setQuantidade(Integer.valueOf(txtQuantidade.getText()));
                produtoOperacao.setValorUnitario(Double.parseDouble(txtPreco.getText()));

                mapVendas.put(produtoOperacao.getCamera().getId(), produtoOperacao);

                carregarTbNovaCompra();
                calculaTotal();

                tbNovaCompra.getSelectionModel().select(produtoOperacao);
                tbNovaCompraAction();

            } catch (SQLException e) {
                logger.error(e.toString());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText("Produto não encontrado");
                alert.setContentText("O produto informado não foi encontrado no sistema.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void btnFinalizarAction(ActionEvent event) throws IOException {
        if (!mapVendas.isEmpty()) {
            Long idFornecedor = FornecedoresPopUpController.display();
            System.out.println(idFornecedor);
            if(idFornecedor != null) {
                try {

                    Compra compra = new Compra();
                    compra.setCameras(mapVendas);

                    compra.setFornecedor(new FornecedorDao().getById(idFornecedor));
                    compra.setDataCompra(LocalDate.now());

                    new CompraDAO().create(compra);

                    ButtonType foo = new ButtonType("Verificar Compra", ButtonBar.ButtonData.YES);
                    ButtonType bar = new ButtonType("Nova Compra", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"",foo, bar);
                    alert.setTitle("Compra Cadastrada");
                    alert.setHeaderText("Informações da compra");
                    alert.setContentText(compra.toString());

                    Optional<ButtonType> result = alert.showAndWait();
                    if(!result.isPresent() || result.get() != ButtonType.YES) {
                        mapVendas.clear();
                        carregarTbNovaCompra();
                        calculaTotal();
                    }else {
                        VendasController.setPreId(compra.getId());
                        switchToCompras(event);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void carregarTbNovaCompra() {
        tbNovaCompra.setItems(FXCollections.observableArrayList(mapVendas.values()));
        tbNovaCompra.refresh();
    }

    private void calculaTotal(){
        Double total = 0d;
        for(ProdutoOperacao produtoOperacao : mapVendas.values()){
            total += produtoOperacao.getSubtotal();
        }

        txtTotal.setText(numberFormat.format(total));
    }

    @FXML
    private void btnRemoverAction(){
        if(tbNovaCompra.getSelectionModel().getSelectedItem() != null){
            ProdutoOperacao produtoOperacao = tbNovaCompra.getSelectionModel().getSelectedItem();
            mapVendas.remove(produtoOperacao.getCamera().getId());

            calculaTotal();
            carregarTbNovaCompra();
        }
    }

    @FXML
    private void btnCancelarAction(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancelar venda");
        alert.setHeaderText("Tem certeza que deseja cancelar a venda?");
        alert.setContentText("Todos os itens adicionados serão perdidos.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            txtId.clear();
            txtPreco.clear();
            txtQuantidade.setText("1");
            txtTotal.clear();
            mapVendas.clear();
            carregarTbNovaCompra();
        }
    }

    @FXML
    private void tbNovaCompraAction(){
        ProdutoOperacao produtoOperacao = tbNovaCompra.getSelectionModel().getSelectedItem();
        if(produtoOperacao != null){
            txtId.setText(produtoOperacao.getCamera().getId().toString());
            txtQuantidade.setText(produtoOperacao.getQuantidade().toString());
            txtPreco.setText(String.valueOf(produtoOperacao.getValorUnitario()));
        }
    }

    @FXML
    private void btnBuscarAction(ActionEvent event) throws IOException {
        if(!txtId.getText().isEmpty()) EstoquePopUpController.setIdToSearch(Long.valueOf(txtId.getText()));
        Long searchedId = EstoquePopUpController.display();
        if(searchedId != null){
            txtId.setText(searchedId.toString());
        }
    }

}
