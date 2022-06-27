package br.edu.femass.controller.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ControllerCommons {


    public static void switchToScene(ActionEvent event, ScenesEnum scenesEnum) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ControllerCommons.class.getResource(scenesEnum.getScene())));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(ControllerCommons.class.getResource("/br/edu/femass/style.css")).toExternalForm());

        String sceneTitle = scenesEnum.toString().charAt(0) + scenesEnum.toString().substring(1).toLowerCase();
        stage.setTitle(sceneTitle.replaceAll("_", " "));
        stage.setScene(scene);

        stage.show();
    }

    public static void closeScene(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static void connectionError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Erro de conexão");
        alert.setContentText("Ocorreu um erro ao conectar ao Banco de Dados");
        alert.showAndWait();
    }
}
