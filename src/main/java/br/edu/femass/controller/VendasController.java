package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.dao.VendaDao;
import br.edu.femass.model.Venda;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

public class VendasController extends TopMenuController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(VendasController.class);
    private static Long preId;
    private final Map<Long, Venda> longVendaMap = new HashMap<>();

    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    @FXML
    private TableColumn<Venda, String> colCliente;

    @FXML
    private TableColumn<Venda, Long> colCodgo;

    @FXML
    private TableColumn<Venda, LocalDate> colData;

    @FXML
    private TableColumn<Venda, Integer> colQuantidade;

    @FXML
    private TableColumn<Venda, String> colTotal;

    @FXML
    private TableView<Venda> tbVendas;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtCliente;

    @FXML
    private TextField txtData, txtId, txtQuantidade, txtTotal;

    @FXML
    void btnBuscaAction() {
        if(!txtId.getText().isEmpty()){
            Venda venda = longVendaMap.get(Long.parseLong(txtId.getText()));
            if(venda != null){
                tbVendas.getSelectionModel().select(venda);
                tbVendas.scrollTo(venda);
                tbVendasOnSelection();
            }
        }
    }

    @FXML
    void btnCancelarAction() {
        Venda venda = tbVendas.getSelectionModel().getSelectedItem();
        if( venda != null){
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar cancelamento");
                alert.setHeaderText(alert.getHeaderText());
                alert.setContentText("Cancelar compra n°" + venda.getId() + " ?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isEmpty() || result.get() != ButtonType.OK) {
                    return;
                }else {
                    new VendaDao().delete(tbVendas.getSelectionModel().getSelectedItem());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        carregarTbVendas();
    }

    private void tbVendasOnSelection() {
        Venda vendaSelecionada = tbVendas.getSelectionModel().getSelectedItem();
        if(vendaSelecionada != null){
            txtId.setText(String.valueOf(vendaSelecionada.getId()));
            txtData.setText(vendaSelecionada.getDataVenda().toString());
            txtQuantidade.setText(String.valueOf(vendaSelecionada.getQuantidade()));
            txtTotal.setText(String.valueOf(vendaSelecionada.getPreco()));
            txtCliente.setText(vendaSelecionada.getCliente().getNomeCompleto());
        }
    }

    public static void setPreId(Long preId) {
        VendasController.preId = preId;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(numberFormat.format(cellData.getValue().getPreco())));
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNomeCompleto()));

        carregarTbVendas();
        if(preId != null){
            tbVendas.getSelectionModel().select(longVendaMap.get(preId));
            tbVendas.scrollTo(longVendaMap.get(preId));
            tbVendasOnSelection();
            preId = null;
        }
    }

    private void carregarTbVendas() {
        List<Venda> vendas = new ArrayList<>();
        try {
            vendas = new VendaDao().read();
        } catch (Exception e) {
            logger.error(e.toString());
            ControllerCommons.connectionError();
        }

        longVendaMap.clear();
        vendas.forEach(item -> longVendaMap.put(item.getId(), item));

        ObservableList<Venda> vendaObservableList = FXCollections.observableArrayList(vendas);
        tbVendas.setItems(vendaObservableList);
        tbVendas.refresh();
    }

    @FXML
    void tbVendasOnClick(MouseEvent event) {
        tbVendasOnSelection();

        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2 & tbVendas.getSelectionModel().getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Detalhes de Venda");
                alert.setHeaderText("Detalhes da Venda");
                alert.setContentText(tbVendas.getSelectionModel().getSelectedItem().toString());
                alert.setResizable(true);
                alert.showAndWait();
            }
        }
    }
}
