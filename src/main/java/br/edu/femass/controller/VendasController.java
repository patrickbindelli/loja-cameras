package br.edu.femass.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class VendasController extends Controller implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(VendasController.class);
    private static Long preId;
    private Map<Long, Venda> longVendaMap = new HashMap<>();
    @FXML
    private TableColumn<Venda, String> colCliente;

    @FXML
    private TableColumn<Venda, Long> colCodgo;

    @FXML
    private TableColumn<Venda, LocalDate> colData;

    @FXML
    private TableColumn<Venda, Integer> colQuantidade;

    @FXML
    private TableColumn<Venda, Double> colTotal;

    @FXML
    private TableView<Venda> tbVendas;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtCliente;

    @FXML
    private TextField txtData, txtId, txtQuantidade, txtTotal;

    @FXML
    void btnBuscaAction(ActionEvent event) {

    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        Venda venda = tbVendas.getSelectionModel().getSelectedItem();
        if( venda != null){
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar cancelamento");
                alert.setHeaderText(alert.getHeaderText());
                alert.setContentText("Cancelar compra n°" + venda.getId() + " ?");

                Optional<ButtonType> result = alert.showAndWait();
                if(!result.isPresent() || result.get() != ButtonType.OK) {
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

    @FXML
    void tbVendasOnClick(){
        tbVendasOnSelection();
    }

    public static void setPreId(Long preId) {
        VendasController.preId = preId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("preco"));
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
            Utils.connectionError();
        }

        longVendaMap.clear();
        vendas.forEach(item -> longVendaMap.put(item.getId(), item));

        ObservableList<Venda> vendaObservableList = FXCollections.observableArrayList(vendas);
        tbVendas.setItems(vendaObservableList);
        tbVendas.refresh();
    }
}
