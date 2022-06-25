package br.edu.femass.controller;

import br.edu.femass.dao.CameraDao;
import br.edu.femass.dao.MarcaDao;
import br.edu.femass.model.Camera;
import br.edu.femass.model.Marca;
import br.edu.femass.model.Tipo;
import br.edu.femass.testes.MainTeste;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NovoProdutoController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(MainTeste.class);

    private final Validator validator = new Validator();

    @FXML
    private Button btnCadastrar, btnNovaMarca, btnSalvarMarca;
    @FXML
    private Label lblNovaMarca;
    @FXML
    private ComboBox<Marca> cbMarca;
    @FXML
    private ComboBox<Tipo> cbTipo;
    @FXML
    private TextArea txtDescricao;
    @FXML
    private TextField txtNome, txtPreco, txtNomeMarca;

    public static void display() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(NovoProdutoController.class.getResource(Scenes.NOVO_PRODUTO.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Novo produto");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.requestFocus();
        stage.showAndWait();
    }

    @FXML
    private void btnCancelarAction(ActionEvent event){
        Utils.closeScene(event);
    }

    @FXML
    private void btnCadastrarAction(ActionEvent event){
        Camera camera = new Camera();
        camera.setNome(txtNome.getText());
        camera.setPreco(Double.parseDouble(txtPreco.getText().replace(',', '.')));
        camera.setDescricao(txtDescricao.getText());
        camera.setMarca(cbMarca.getSelectionModel().getSelectedItem());
        camera.setTipo(cbTipo.getSelectionModel().getSelectedItem());
        try {
            new CameraDao().create(camera);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Utils.closeScene(event);
    }

    @FXML
    private void btnNovaMarcaAction(){
        if(txtNomeMarca.isDisabled()){
            lblNovaMarca.setDisable(false);
            txtNomeMarca.setDisable(false);
            cbMarca.setDisable(true);
            btnNovaMarca.setText("Cancelar");
        }else{
            lblNovaMarca.setDisable(true);
            txtNomeMarca.setDisable(true);
            txtNomeMarca.setText("");
            btnSalvarMarca.setDisable(true);
            cbMarca.setDisable(false);
            btnNovaMarca.setText("Nova Marca");
        }
    }

    @FXML
    private void btnSalvarMarcaAction(){
        Marca marca = new Marca();
        marca.setNome(txtNomeMarca.getText());

        try {
            new MarcaDao().create(marca);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        carregaMarcas();
        cbMarca.getSelectionModel().select(marca);
        btnNovaMarcaAction();
    }

    @FXML
    private void btnDeletarMarcaAction(){
        if(cbMarca.getSelectionModel().getSelectedItem() != null){
            Marca marca = cbMarca.getSelectionModel().getSelectedItem();
            try {
                new MarcaDao().delete(marca);
                carregaMarcas();
            } catch (SQLException e) {
                logger.error(e.toString());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Não é possível deletar uma marca que possui produtos cadastrados.");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validator.createCheck()
                .dependsOn("nome", txtNome.textProperty())
                .dependsOn("preco", txtPreco.textProperty())
                .dependsOn("descricao", txtDescricao.textProperty())
                .dependsOn("marca", cbMarca.getSelectionModel().selectedItemProperty())
                .dependsOn("tipo", cbTipo.getSelectionModel().selectedItemProperty())
                .withMethod(c -> {
                    String nome = c.get("nome");
                    String preco = c.get("preco");
                    String descricao = c.get("descricao");
                    Marca marca = c.get("marca");
                    Tipo tipo = c.get("tipo");

                    if(nome.isEmpty() || preco.isEmpty() || descricao.isEmpty() || marca == null || tipo == null || !preco.matches("^\\d*.\\d+$")){
                        StringBuilder erroMsg = new StringBuilder("Todos os campos precisam ser preenchidos corretamente");
                        if(!preco.matches("^\\d+.\\d+$")){
                            erroMsg.append("\nPreço precisa ser um número válido.");
                        }
                        c.error(erroMsg.toString());
                        btnCadastrar.setDisable(true);
                    }else{btnCadastrar.setDisable(false);}
                })
                .decorates(btnCadastrar)
                .immediate();

        validator.createCheck()
                .dependsOn("marca", txtNomeMarca.textProperty())
                .withMethod(c -> {
                    String marca = c.get("marca");
                    btnSalvarMarca.setDisable(marca.isEmpty());
                })
                .decorates(txtNomeMarca)
                .immediate();

        carregaMarcas();
        carregaTipos();
}

    private void carregaTipos(){
        ObservableList<Tipo> tipoObservableList = FXCollections.observableArrayList(Tipo.values());
        cbTipo.setItems(tipoObservableList);
    }

    private void carregaMarcas(){
        List<Marca> marcaList;

        try {
            marcaList = new MarcaDao().read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ObservableList<Marca> marcaObservableList = FXCollections.observableList(marcaList);
        cbMarca.setItems(marcaObservableList);
    }
}
