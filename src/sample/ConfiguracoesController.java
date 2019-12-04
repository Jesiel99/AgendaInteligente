package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfiguracoesController implements Initializable {

    @FXML
    TextField txtInicio, txtFim;
    @FXML
    private TableView<HorariosIndisponiveis> hrsTable;
    @FXML
    private TableColumn columnInicio, columnFim;

    private JSON json;
    private ObservableList<HorariosIndisponiveis> hrsList = FXCollections.observableArrayList();

    public ConfiguracoesController() throws IOException {
        json = new JSON("configuracao");
        System.out.println(json.lerHorario());
        try {
            hrsList = FXCollections.observableArrayList(json.lerHorario());
        } catch (Exception ignore) {}
    }

    private void refreshTable() {
        try {
            columnInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        } catch (Exception ignore) {}
        try {
            columnFim.setCellValueFactory(new PropertyValueFactory<>("fim"));
        } catch (Exception ignore) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            hrsList = FXCollections.observableArrayList(json.lerHorario());
            refreshTable();
            hrsTable.setItems(hrsList);
        } catch (Exception ignore) {}
    }

    public void addHorarioIndisponivel(ActionEvent actionEvent) {

        hrsList.add(new HorariosIndisponiveis(txtInicio.getText(), txtFim.getText()));
        refreshTable();
        hrsTable.setItems(hrsList);
        json.gravarHorario(hrsList);
    }

    public ObservableList<HorariosIndisponiveis> getHrsList() {
        return hrsList;
    }

    public void setHrsList(ObservableList<HorariosIndisponiveis> hrsList) {
        hrsList = hrsList;
    }
}
