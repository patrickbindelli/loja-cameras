package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.dao.ClienteDao;
import br.edu.femass.dao.FornecedorDao;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Fornecedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FornecedoresController extends TopMenuController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(NovoClienteController.class);
    @FXML
    private Button btnClose;

    @FXML
    private TableColumn<Fornecedor, String> colCNPJ;

    @FXML
    private TableColumn<Fornecedor, Long> colCodgo;

    @FXML
    private TableColumn<Fornecedor, String> colNome;

    @FXML
    private TableColumn<Fornecedor, String> colTelefone;

    @FXML
    private TableView<Fornecedor> tbFornecedores;

    @FXML
    private TextField txtCNPJ, txtId, txtNome, txtTelefone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        carregaTbFornecedores();
    }

    private void carregaTbFornecedores(){
        List<Fornecedor> fornecedorList = new ArrayList<>();
        try {
            fornecedorList = new FornecedorDao().read();
        } catch (Exception e) {
            logger.error(e.toString());
            ControllerCommons.connectionError();
        }
        ObservableList<Fornecedor> fornecedorObservableList = FXCollections.observableArrayList(fornecedorList);
        tbFornecedores.setItems(fornecedorObservableList);
        tbFornecedores.refresh();
    }

    @FXML
    private void btnCadastrarAction() throws IOException {
        NovoFornecedorController.display();
        carregaTbFornecedores();
    }

    @FXML
    private void btnRemoverAction(){
        Fornecedor fornecedor = tbFornecedores.getSelectionModel().getSelectedItem();
        if(fornecedor != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancelar venda");
            alert.setHeaderText("Tem certeza que deseja excluir esse fornecedor?");
            alert.setContentText("Todos os dados relacionados a ele serão perdidos.");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    new FornecedorDao().delete(fornecedor);
                    carregaTbFornecedores();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private void btnBuscaAction(){
        tbFornecedores.getItems().stream()
                .filter(item -> item.getId() == Integer.parseInt(txtId.getText()))
                .findAny()
                .ifPresent(item -> {
                    tbFornecedores.getSelectionModel().select(item);
                    tbFornecedores.scrollTo(item);
                    tbFornecedoresOnClick();
                });
    }

    @FXML
    private void btnAtualizarAction(){
        Fornecedor fornecedor = tbFornecedores.getSelectionModel().getSelectedItem();
        if(fornecedor != null && Objects.equals(txtId.getText(), String.valueOf(fornecedor.getId()))){
            fornecedor.setNome(txtNome.getText());
            fornecedor.setCnpj(txtCNPJ.getText());
            fornecedor.setTelefone(txtTelefone.getText());

            try {
                new FornecedorDao().update(fornecedor);
            } catch (Exception e) {
                logger.error(e.toString());
            }

            carregaTbFornecedores();
        }
    }

    @FXML
    private void tbFornecedoresOnClick(){
        Fornecedor fornecedor = tbFornecedores.getSelectionModel().getSelectedItem();
        if(fornecedor != null){
            tbFornecedores.getSelectionModel().select(fornecedor);
            txtId.setText(String.valueOf(fornecedor.getId()));
            txtNome.setText(fornecedor.getNome());
            txtCNPJ.setText(fornecedor.getCnpj());
            txtTelefone.setText(fornecedor.getTelefone());
        }
    }
}
