package br.edu.femass.controller;

import br.edu.femass.dao.CameraDao;
import br.edu.femass.model.Camera;
import br.edu.femass.model.Marca;
import br.edu.femass.model.Tipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    private TableColumn<Camera, Double> colValor;

    private static Long idToSearch;
    private static Stage stage;
    private final Map<Long, Camera> longCameraMap = new HashMap<>();

    public static void setIdToSearch(Long idToSearch) {
        EstoquePopUpController.idToSearch = idToSearch;
    }

    public static Long display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EstoquePopUpController.class.getResource(Scenes.ESTOQUE_POPUP.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("Selecionar Produto");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.requestFocus();
        stage.showAndWait();

        return idToSearch;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("preco"));

        carregaTabelaEstoque();

        System.out.println(idToSearch);
        if(idToSearch != null){
            tbEstoque.getSelectionModel().select(longCameraMap.get(idToSearch));
            tbEstoque.scrollTo(longCameraMap.get(idToSearch));
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
            idToSearch = tbEstoque.getSelectionModel().getSelectedItem().getId();
        }
        Utils.closeScene(event);
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        Utils.closeScene(event);
    }

    @FXML
    void tbEstoqueOnClick(MouseEvent event) {
        if(tbEstoque.getSelectionModel().getSelectedItem() != null) {
            Camera camera = tbEstoque.getSelectionModel().getSelectedItem();
            idToSearch = camera.getId();
        }

        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                stage.close();
            }
        }
    }
}
