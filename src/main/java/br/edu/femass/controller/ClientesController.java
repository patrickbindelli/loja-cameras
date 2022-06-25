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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientesController extends Controller implements Initializable {

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
    private TextField txtCPF, txtId, txtNome, txtSobrenome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));

        carregaTbClientes();
    }

    @FXML
    private void tbClientesOnClick(){

    }

    @FXML
    private void btnCadastrarAction(){

    }

    @FXML
    private void btnRemoverAction(){

    }

    @FXML
    private void btnBuscaAction(){

    }

    @FXML
    private void btnAtualizarAction(){

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
