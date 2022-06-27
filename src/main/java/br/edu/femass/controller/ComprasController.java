package br.edu.femass.controller;

import br.edu.femass.controller.Utils.ControllerCommons;
import br.edu.femass.dao.CompraDAO;
import br.edu.femass.dao.VendaDao;
import br.edu.femass.model.Compra;
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

public class ComprasController extends TopMenuController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(VendasController.class);
    private static Long preId;
    private final Map<Long, Compra> longCompraHashMap = new HashMap<>();

    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    @FXML
    private TableColumn<Compra, String> colFornecedor;

    @FXML
    private TableColumn<Compra, Long> colCodgo;

    @FXML
    private TableColumn<Compra, LocalDate> colData;

    @FXML
    private TableColumn<Compra, Integer> colQuantidade;

    @FXML
    private TableColumn<Compra, String> colTotal;

    @FXML
    private TableView<Compra> tbCompras;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtFornecedor;

    @FXML
    private TextField txtData, txtId, txtQuantidade, txtTotal;

    @FXML
    void btnBuscaAction(ActionEvent event) {
        if(!txtId.getText().isEmpty()){
            Compra compra = longCompraHashMap.get(Long.parseLong(txtId.getText()));
            if(compra != null){
                tbCompras.getSelectionModel().select(compra);
                tbCompras.scrollTo(compra);
                tbComprasOnSelection();
            }
        }
    }

    @FXML
    void btnCancelarAction(ActionEvent event) {
        Compra compra = tbCompras.getSelectionModel().getSelectedItem();
        if( compra != null){
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar cancelamento");
                alert.setHeaderText(alert.getHeaderText());
                alert.setContentText("Cancelar compra n°" + compra.getId() + " ?");

                Optional<ButtonType> result = alert.showAndWait();
                if(!result.isPresent() || result.get() != ButtonType.OK) {
                    return;
                }else {
                    new CompraDAO().delete(tbCompras.getSelectionModel().getSelectedItem());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        carregarTbVendas();
    }

    private void tbComprasOnSelection() {
        Compra compraSelecionada = tbCompras.getSelectionModel().getSelectedItem();
        if(compraSelecionada != null){
            txtId.setText(String.valueOf(compraSelecionada.getId()));
            txtData.setText(compraSelecionada.getDataCompra().toString());
            txtQuantidade.setText(String.valueOf(compraSelecionada.getQuantidade()));
            txtTotal.setText(String.valueOf(compraSelecionada.getPreco()));
            txtFornecedor.setText(compraSelecionada.getFornecedor().getNome());
        }
    }

    @FXML
    void tbComprasOnClick(MouseEvent event){
        tbComprasOnSelection();

        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2 & tbCompras.getSelectionModel().getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Detalhes de Compra");
                alert.setHeaderText("Detalhes da Compra");
                alert.setContentText(tbCompras.getSelectionModel().getSelectedItem().toString());
                alert.setResizable(true);
                alert.showAndWait();
            }
        }
    }

    public static void setPreId(Long preId) {
        ComprasController.preId = preId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodgo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataCompra"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(numberFormat.format(cellData.getValue().getPreco())));
        colFornecedor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFornecedor().getNome()));

        carregarTbVendas();
        if(preId != null){
            tbCompras.getSelectionModel().select(longCompraHashMap.get(preId));
            tbCompras.scrollTo(longCompraHashMap.get(preId));
            tbComprasOnSelection();
            preId = null;
        }
    }

    private void carregarTbVendas() {
        List<Compra> compras = new ArrayList<>();
        try {
            compras = new CompraDAO().read();
            System.out.printf(compras.toString());
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            ControllerCommons.connectionError();
        }

        longCompraHashMap.clear();
        compras.forEach(item -> longCompraHashMap.put(item.getId(), item));

        ObservableList<Compra> vendaObservableList = FXCollections.observableArrayList(compras);
        tbCompras.setItems(vendaObservableList);
        tbCompras.refresh();
    }
}
