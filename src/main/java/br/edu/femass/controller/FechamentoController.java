package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.controller.Utils.ScenesEnum;
import br.edu.femass.dao.CompraDAO;
import br.edu.femass.dao.VendaDao;
import br.edu.femass.model.Compra;
import br.edu.femass.model.Venda;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FechamentoController implements Initializable {
    @FXML
    private DatePicker dtFechamento;

    @FXML
    private Label lblCompras;

    @FXML
    private Label lblVendas;

    @FXML
    private TextField txtTotalCompras;

    @FXML
    private TextField txtTotalSaldo;

    @FXML
    private TextField txtTotalVendas;

    public static void display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EstoquePopUpController.class.getResource(ScenesEnum.FECHAMENTO.getScene()));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Fechamento de Caixa");
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.requestFocus();
        stage.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dtFechamento.setValue(LocalDate.now());
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        ControllerCommons.closeScene(event);
    }

    @FXML
    void btnVerificarAction(ActionEvent event) {
        List<Venda> vendaList = new ArrayList<>();
        List<Compra> compraList = new ArrayList<>();
        double totalVendas = 0d, totalCompras = 0d, totalSaldo = 0;

        try {
            vendaList = new VendaDao().getByDate(dtFechamento.getValue());
            compraList = new CompraDAO().getByDate(dtFechamento.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for(Venda venda : vendaList){
            totalVendas += venda.getPreco();
        }

        for(Compra compra : compraList){
            totalCompras += compra.getPreco();
        }

        totalSaldo = totalVendas - totalCompras;

        lblCompras.setText(String.valueOf(compraList.size()));
        lblVendas.setText(String.valueOf(vendaList.size()));

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        txtTotalCompras.setText(numberFormat.format(totalCompras));
        txtTotalVendas.setText(numberFormat.format(totalVendas));
        txtTotalSaldo.setText(numberFormat.format(totalSaldo));

    }
}
