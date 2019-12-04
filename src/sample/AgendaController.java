package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class AgendaController implements Initializable {

    private ObservableList<Tarefa> tarefaList = FXCollections.observableArrayList();
    private final String tasksfileName;
    private JSON json;

    @FXML
    private TextField txtInicio, txtNome, txtDuracao, txtTipo;
    @FXML
    private ChoiceBox chcbxPrioridade, chcbxDificuldade;
    @FXML
    private TableView<Tarefa> taskTable;
    @FXML
    private TableColumn columnInicio, columnFim, columnDuracao, columnNome, columnPrioridade, columnDificuldade, columnTipo;
    @FXML
    private Label lblLCM;

    public AgendaController() throws IOException {

        tasksfileName = "tasks";
        json = new JSON(tasksfileName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> tipoPrioridade = FXCollections.observableArrayList();
        tipoPrioridade.add("P1");
        tipoPrioridade.add("P2");
        tipoPrioridade.add("P3");
        chcbxPrioridade.setItems(tipoPrioridade);
        ObservableList<String> dificuldades = FXCollections.observableArrayList();
        dificuldades.add("TRIVIAL");
        dificuldades.add("FACIL");
        dificuldades.add("MEDIA");
        dificuldades.add("DIFICIL");
        chcbxDificuldade.setItems(dificuldades);
        try {
            tarefaList = FXCollections.observableArrayList(json.ler());
        } catch (Exception ignore) {}
        refreshTable();
        taskTable.setItems(tarefaList);
    }

    private void push(int index, long minutes) {
        for (int i = index; i < tarefaList.size(); i++) {
            Tarefa editedTarefa = tarefaList.get(i);
            editedTarefa.setInicio(editedTarefa.getInicio().plusMinutes(minutes));
            editedTarefa.setFim(editedTarefa.getFim().plusMinutes(minutes));
            tarefaList.set(i, editedTarefa);
        }
        refreshTable();
        taskTable.setItems(tarefaList);
    }

    private boolean isAvailableHr(Tarefa tarefa) throws IOException {
        ObservableList<HorariosIndisponiveis> hrsAnavailableList = new ConfiguracoesController().getHrsList();
        for (HorariosIndisponiveis hr : hrsAnavailableList) {
            int hrMinuteInicio = hr.getInicio().getMinute() + hr.getInicio().getHour()*60;
            int hrMinuteFim = hr.getFim().getMinute() + hr.getFim().getHour()*60;
            if (hr.getFim().isBefore(hr.getInicio())) {
                hrMinuteFim += 60*24;
            }
            int taskMinuteInicio = tarefa.getInicio().getMinute() + tarefa.getInicio().getHour()*60;
            int taskMinuteFim = tarefa.getFim().getMinute() + tarefa.getFim().getHour()*60;
            if (tarefa.getFim().isBefore(tarefa.getInicio())) {
                taskMinuteFim += 60*24;
            }
            if (hrMinuteFim - 24*60 > taskMinuteInicio) {
                taskMinuteInicio += 24*60;
                taskMinuteFim += 24*60;
            } else if (taskMinuteFim -24*60 > hrMinuteInicio) {
                hrMinuteInicio += 24*60;
                hrMinuteFim += 24*60;
            }
            if (taskMinuteInicio > hrMinuteInicio && taskMinuteFim < hrMinuteFim
                    || taskMinuteFim > hrMinuteInicio && taskMinuteFim < hrMinuteFim)
                return false;
        }
        return true;
    }

    @FXML
    private void addTask() throws IOException  {
        LocalDateTime NOW = LocalDateTime.now();
        Tarefa tarefa = new Tarefa();
        tarefa.setInicio(Cast.stringToLocalDateTime(txtInicio.getText()));
        tarefa.setDuracao(txtDuracao.getText());
        tarefa.setNome(txtNome.getText());
        tarefa.setTipo(txtTipo.getText());
        tarefa.setDificuldade((String) chcbxDificuldade.getValue());
        tarefa.setPrioridade((String) chcbxPrioridade.getValue());

        if (tarefa.getInicio().isBefore(NOW)) {
            openWindow("ERRO", "ERRO", 530, 80);
        } else if (!isAvailableHr(tarefa)) {
            openWindow("ERRO", "ERRO", 530, 80);
        } else if (tarefaList.isEmpty() || tarefaList.get(tarefaList.size() - 1).getFim().isBefore(tarefa.getInicio())) {
            tarefaList.add(tarefa);
        } else {
            int i;
            for (i = 0; i < tarefaList.size(); i++) {
                if (tarefaList.get(i).getFim().isAfter(tarefa.getInicio())) {
                    tarefaList.add(i, tarefa);
                    break;
                } else if (tarefaList.get(i).getFim().isEqual(tarefa.getInicio())) {
                    int j;
                    try {
                        for (j = i; tarefaList.get(j).getFim().isEqual(tarefa.getInicio()); j++) ;
                        tarefaList.add(j, tarefa);
                    } catch (Exception e) {
                        tarefaList.add(tarefa);
                    }
                    break;
                }
            }
            if (tarefa.getFim().isAfter(tarefaList.get(i+1).getInicio())) {
                push(i+1, tarefaList.get(i+1).getInicio().until(tarefaList.get(i).getFim(), ChronoUnit.MINUTES));
            }
        }
        refreshTable();
        taskTable.setItems(tarefaList);
        json.gravar(tarefaList);
        taskTable.setEditable(true);
//        columnNome.setCellValueFactory(TextFieldTableCell.forTableColumn());
//        columnTipo.setCellValueFactory(TextFieldTableCell.forTableColumn());
    }

    public void onEditChanged(TableColumn.CellEditEvent cellEditEvent) {
        Tarefa tarefa = taskTable.getSelectionModel().getSelectedItem();
        tarefa.setNome((String) cellEditEvent.getNewValue());
    }

    private void refreshTable() {
        try {
            columnInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        } catch (Exception ignore) {}
        try {
            columnDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        } catch (Exception ignore) {
        }
        try {
            columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        } catch (Exception ignore) {
        }
        try {
            columnDificuldade.setCellValueFactory(new PropertyValueFactory<>("dificuldade"));
        } catch (Exception ignore) {
        }
        try {
            columnPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
        } catch (Exception ignore) {
        }
        try {
            columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        } catch (Exception ignore) {
        }
        try {
            columnFim.setCellValueFactory(new PropertyValueFactory<>("fim"));
        } catch (Exception ignore) {
        }
    }



    private void editableCols() {
//        columnNome.setCellFactory(TextFieldTableCell.forTableColumn());
//        columnNome.getTableView().getRowFactory()
    }



    private void openWindow(String fxmlName, String stageTitle, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlName + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.show();
    }

    public void openWindow(ActionEvent actionEvent) throws IOException {
        openWindow("configuracoes", "Configuracoes", 400, 340);
    }



}