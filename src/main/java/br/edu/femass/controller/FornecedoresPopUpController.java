package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import br.edu.femass.dao.FornecedorDao;
import br.edu.femass.model.Camera;
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
import java.util.*;

public class FornecedoresPopUpController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(NovoClienteController.class);

    private static Long idToSearch;
    private static Long idToReturn;
    private Long intenalIdToSearch;
    private static Stage stage;
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

    public static Long display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EstoquePopUpController.class.getResource(ScenesEnum.FORNECEDORES_POPUP.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("Selecionar Fornecedor");
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
    void btnAdicionarAction(ActionEvent event) {
        if(tbFornecedores.getSelectionModel().getSelectedItem() != null){
            idToReturn = tbFornecedores.getSelectionModel().getSelectedItem().getId();
        }
        ControllerCommons.closeScene(event);
    }

    @FXML
    private void btnCadastrarAction() throws IOException {
        NovoFornecedorController.display();
        carregaTbFornecedores();
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        ControllerCommons.closeScene(event);
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
    void tbFornecedoresOnClick(MouseEvent event) {
        if(tbFornecedores.getSelectionModel().getSelectedItem() != null) {
            Fornecedor fornecedor = tbFornecedores.getSelectionModel().getSelectedItem();
            idToReturn = fornecedor.getId();
        }

        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                stage.close();
            }
        }
    }
}
