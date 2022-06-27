package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import br.edu.femass.dao.ClienteDao;
import br.edu.femass.dao.FornecedorDao;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Fornecedor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NovoFornecedorController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(NovoClienteController.class);
    private final Validator validator = new Validator();

    public static void display() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(NovoProdutoController.class.getResource(ScenesEnum.NOVO_FORNECEDOR.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cadastro de Fornecedor");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.requestFocus();
        stage.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validator.createCheck()
                .dependsOn("nome", txtNome.textProperty())
                .dependsOn("cnpj", txtCnpj.textProperty())
                .dependsOn("telefone", txtTelefone.textProperty())
                .withMethod(c -> {
                    String nome = c.get("nome");
                    String cnpj = c.get("cnpj");
                    String telefone = c.get("telefone");

                    String erro = "";
                    boolean invalido = nome.isEmpty() || cnpj.isEmpty() || telefone.isEmpty();

                    if(invalido) erro += "Há campos não preenchidos\n";

                    if(!cnpj.matches("\\d*")){
                        txtCnpj.setText(cnpj.replaceAll("[^\\d]", ""));
                    }

                    if(!telefone.matches("\\d*")){
                        txtTelefone.setText(cnpj.replaceAll("[^\\d]", ""));
                    }

                    if(cnpj.length() > 14){
                        cnpj = cnpj.substring(0, 14);
                        txtCnpj.setText(cnpj);
                    }

                    if(telefone.length() > 11){
                        telefone = telefone.substring(0, 11);
                        txtTelefone.setText(telefone);
                    }

                    if(!isCnpj(cnpj)){
                        erro += "CNPJ Inválido.\n";
                    }

                    if (!erro.isEmpty()) c.error(erro);

                    btnCadastrar.setDisable(invalido || !isCnpj(cnpj));
                })
                .decorates(btnCadastrar)
                .immediate();
    }

    @FXML
    private Button btnCadastrar;

    @FXML
    private TextField txtCnpj, txtNome, txtTelefone;

    @FXML
    void btnCadastrarAction(ActionEvent event) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(txtNome.getText());
        fornecedor.setCnpj(txtCnpj.getText());
        fornecedor.setTelefone(txtTelefone.getText());

        try {
            new FornecedorDao().create(fornecedor);
        } catch (Exception e) {
            logger.error(e.toString());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Erro ao cadastrar fornecedor. CNPJ pode já existir no sistema.");
            alert.showAndWait();
        }

        ControllerCommons.closeScene(event);
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        ControllerCommons.closeScene(event);
    }

    private boolean isCnpj(String CNPJ){
        return (CNPJ.length() == 14);
    }

}
