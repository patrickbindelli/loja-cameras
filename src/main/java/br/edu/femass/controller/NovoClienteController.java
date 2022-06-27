package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import br.edu.femass.dao.ClienteDao;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Marca;
import br.edu.femass.model.Tipo;
import br.edu.femass.testes.MainTeste;
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
import org.postgresql.core.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class NovoClienteController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(NovoClienteController.class);
    private final Validator validator = new Validator();

    @FXML
    private Button btnCadastrar;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSobrenome;

    @FXML
    private TextField txtTelefone;

    public static void display() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(NovoProdutoController.class.getResource(ScenesEnum.NOVO_CLIENTE.getScene()));
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
                .dependsOn("sobrenome", txtSobrenome.textProperty())
                .dependsOn("cpf", txtCpf.textProperty())
                .dependsOn("telefone", txtTelefone.textProperty())
                .withMethod(c -> {
                    String nome = c.get("nome");
                    String sobrenome = c.get("sobrenome");
                    String cpf = c.get("cpf");
                    String telefone = c.get("telefone");

                    String erro = "";
                    boolean invalido = nome.isEmpty() || sobrenome.isEmpty() || cpf.isEmpty() || telefone.isEmpty();

                    if(invalido) erro += "Há campos não preenchidos\n";

                    if(!cpf.matches("\\d*")){
                        txtCpf.setText(cpf.replaceAll("[^\\d]", ""));
                    }

                    if(!telefone.matches("\\d*")){
                        txtTelefone.setText(cpf.replaceAll("[^\\d]", ""));
                    }

                    if(cpf.length() > 11){
                        cpf = cpf.substring(0, 11);
                        txtCpf.setText(cpf);
                    }

                    if(telefone.length() > 11){
                        telefone = telefone.substring(0, 11);
                        txtTelefone.setText(telefone);
                    }

                    if(!isCPF(cpf)){
                        erro += "CPF Inválido.\n";
                    }

                    if (!erro.isEmpty()) c.error(erro);

                    btnCadastrar.setDisable(invalido || !isCPF(cpf));
                })
                .decorates(btnCadastrar)
                .immediate();
    }

    @FXML
    void btnCadastrarAction(ActionEvent event) {
        Cliente cliente = new Cliente();
        cliente.setNome(txtNome.getText());
        cliente.setSobrenome(txtSobrenome.getText());
        cliente.setCpf(txtCpf.getText());
        cliente.setTelefone(txtTelefone.getText());

        try {
            new ClienteDao().create(cliente);
        } catch (Exception e) {
            logger.error(e.toString());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Erro ao cadastrar cliente. CPF pode já existir no sistema.");
            alert.showAndWait();
        }

        ControllerCommons.closeScene(event);
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        ControllerCommons.closeScene(event);
    }

    public static boolean isCPF(String CPF) {
        return CPF.length() == 11;
    }

    public static String imprimeCPF(String CPF) {
        return(CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." +
                CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
    }

}
