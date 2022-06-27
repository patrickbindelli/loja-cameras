package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import br.edu.femass.dao.ClienteDao;
import br.edu.femass.dao.FornecedorDao;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Fornecedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientesPopUpController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(ClientesPopUpController.class);

    private static Long idToSearch;
    private static Long idToReturn;
    private Long intenalIdToSearch;
    private static Stage stage;
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

    public static Long display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EstoquePopUpController.class.getResource(ScenesEnum.CLIENTES_POPUP.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("Selecionar Cliente");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.requestFocus();
        stage.showAndWait();

        return idToReturn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        carregaTbClientes();
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

    @FXML
    void tbClientesOnClick(MouseEvent event) {
        if(tbClientes.getSelectionModel().getSelectedItem() != null) {
            Cliente cliente = tbClientes.getSelectionModel().getSelectedItem();
            idToReturn = cliente.getId();
        }

        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                stage.close();
            }
        }
    }

    @FXML
    void btnAdicionarAction(ActionEvent event) {
        if(tbClientes.getSelectionModel().getSelectedItem() != null){
            idToReturn = tbClientes.getSelectionModel().getSelectedItem().getId();
        }
        ControllerCommons.closeScene(event);
    }

    @FXML
    private void btnCadastrarAction() throws IOException {
        NovoFornecedorController.display();
        carregaTbClientes();
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        ControllerCommons.closeScene(event);
    }

    @FXML
    private void btnRemoverAction(){
        Cliente cliente = tbClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancelar venda");
            alert.setHeaderText("Tem certeza que deseja excluir esse fornecedor?");
            alert.setContentText("Todos os dados relacionados a ele serão perdidos.");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    new ClienteDao().delete(cliente);
                    carregaTbClientes();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
