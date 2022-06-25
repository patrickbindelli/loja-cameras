package br.edu.femass.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Utils {


    public static void switchToScene(ActionEvent event, Scenes scenes) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Utils.class.getResource(scenes.getScene())));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(Utils.class.getResource("/br/edu/femass/style.css")).toExternalForm());

        String sceneTitle = scenes.toString().charAt(0) + scenes.toString().substring(1).toLowerCase();
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
