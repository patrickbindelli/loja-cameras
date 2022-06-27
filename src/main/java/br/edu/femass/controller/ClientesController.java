package br.edu.femass.controller;

import br.edu.femass.dao.ClienteDao;
import br.edu.femass.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientesController extends TopMenuController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(NovoClienteController.class);
    @FXML
    private TableColumn<Cliente, String> colCPF;

    @FXML
    private TableColumn<Cliente, Long> colCodgo;

    @FXML
    private TableColumn<Cliente, String> colNome;

    @FXML
    private TableColumn<Cliente, String> colSobrenome;

    @FXML
    private TableColumn<Cliente, String> colTelefone;

    @FXML
    private TableView<Cliente> tbClientes;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtCPF, txtId, txtNome, txtSobrenome, txtTelefone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        carregaTbClientes();
    }

    @FXML
    private void tbClientesOnClick(){
        Cliente cliente = tbClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            tbClientes.getSelectionModel().select(cliente);
            txtId.setText(String.valueOf(cliente.getId()));
            txtNome.setText(cliente.getNome());
            txtSobrenome.setText(cliente.getSobrenome());
            txtCPF.setText(cliente.getCpf());
            txtTelefone.setText(cliente.getTelefone());
        }
    }

    @FXML
    private void btnCadastrarAction() throws IOException {
        NovoClienteController.display();
        carregaTbClientes();
    }

    @FXML
    private void btnRemoverAction(){
        Cliente cliente = tbClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            try {
                new ClienteDao().delete(cliente);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void btnBuscaAction(){
        tbClientes.getItems().stream()
                .filter(item -> item.getId() == Integer.parseInt(txtId.getText()))
                .findAny()
                .ifPresent(item -> {
                    tbClientes.getSelectionModel().select(item);
                    tbClientes.scrollTo(item);
                    tbClientesOnClick();
                });
    }

    @FXML
    private void btnAtualizarAction(){
        Cliente cliente = tbClientes.getSelectionModel().getSelectedItem();
        if(cliente != null && Objects.equals(txtId.getText(), String.valueOf(cliente.getId()))){
            cliente.setNome(txtNome.getText());
            cliente.setSobrenome(txtSobrenome.getText());
            cliente.setCpf(txtCPF.getText());
            cliente.setTelefone(txtTelefone.getText());

            try {
                new ClienteDao().update(cliente);
            } catch (Exception e) {
                logger.error(e.toString());
            }

            carregaTbClientes();
        }
    }

    private void carregaTbClientes(){
        List<Cliente> clienteList = new ArrayList<>();
        try {
            clienteList = new ClienteDao().read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ObservableList<Cliente> clienteObservableList = FXCollections.observableArrayList(clienteList);
        tbClientes.setItems(clienteObservableList);
        tbClientes.refresh();
    }
}
