package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import br.edu.femass.dao.CameraDao;
import br.edu.femass.model.Camera;
import br.edu.femass.model.Marca;
import br.edu.femass.model.Tipo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EstoquePopUpController implements Initializable {
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

    private static Long idToSearch;
    private static Long idToReturn;
    private static Stage stage;
    private final Map<Long, Camera> longCameraMap = new HashMap<>();
    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    public static void setIdToSearch(Long idToSearch) {
        EstoquePopUpController.idToSearch = idToSearch;
    }

    public static Long display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EstoquePopUpController.class.getResource(ScenesEnum.ESTOQUE_POPUP.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("Selecionar Produto");
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
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(numberFormat.format(cellData.getValue().getPreco())));

        carregaTabelaEstoque();

        Long intenalIdToSearch = idToSearch;
        idToSearch = null;

        if(intenalIdToSearch != null){
            tbEstoque.getSelectionModel().select(longCameraMap.get(intenalIdToSearch));
            tbEstoque.scrollTo(longCameraMap.get(intenalIdToSearch));
        }
    }

    private void carregaTabelaEstoque() {
        try {
            List<Camera> cameraList = new CameraDao().read();
            cameraList.forEach(item -> longCameraMap.put(item.getId(), item));
            ObservableList<Camera> observableListCameras = FXCollections.observableArrayList(cameraList);
            tbEstoque.setItems(observableListCameras);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnAdicionarAction(ActionEvent event) {
        if(tbEstoque.getSelectionModel().getSelectedItem() != null){
            idToReturn = tbEstoque.getSelectionModel().getSelectedItem().getId();
        }
        ControllerCommons.closeScene(event);
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        ControllerCommons.closeScene(event);
    }

    @FXML
    void tbEstoqueOnClick(MouseEvent event) {
        if(tbEstoque.getSelectionModel().getSelectedItem() != null) {
            Camera camera = tbEstoque.getSelectionModel().getSelectedItem();
            idToReturn = camera.getId();
        }

        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                stage.close();
            }
        }
    }
}
