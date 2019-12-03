package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class AgendaController implements Initializable {

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private String[] style = {"status-one", "status-two", "status-three",
            "status-four", "status-five", "status-six", "status-seven", "status-eight", "status-nine", "status-ten",
            "status-eleven", "status-twelve"};
    private List<String> styleList;
    private final String tasksfileName;
    private JSON json;

    @FXML
    private TextField txtInicio, txtNome, txtDuracao, txtDificuldade, txtTipo, txtPrioridade;
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn columnInicio, columnFim, columnDuracao, columnNome, columnPrioridade, columnDificuldade, columnTipo;
    @FXML
    private Label lblLCM;

    public AgendaController() throws IOException {

        tasksfileName = "tasks";
        json = new JSON(tasksfileName);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taskTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtInicio.setText(String.valueOf(newSelection.getInicio()));
                    }
                });
            }
        });

    }

    private void openError() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ERRO.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 80);
        Stage stage = new Stage();
        stage.setTitle("ERRO");
        stage.setScene(scene);
        stage.show();
    }

    public void openConficuracao(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("configuracoes.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 80);
        Stage stage = new Stage();
        stage.setTitle("Configuraçao");
        stage.setScene(scene);
        stage.show();
    }

    private void push(int index, long minutes) {
        for (int i = index; i < taskList.size(); i++) {
            System.out.println(minutes);
            Task editedTask = taskList.get(i);
            System.out.println(editedTask.getInicio());
            editedTask.setInicio(editedTask.getInicio().plusMinutes(minutes));
            System.out.println(editedTask.getInicio());
            editedTask.setFim(editedTask.getFim().plusMinutes(minutes));
            taskList.set(i, editedTask);
        }
        refreshTable();
        taskTable.setItems(taskList);

    }

    @FXML
    private void addTask() throws IOException  {

        LocalDateTime NOW = LocalDateTime.now();
        Task task = new Task();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        task.setInicio(LocalDateTime.parse(txtInicio.getText(), formatter));
        task.setDuracao(txtDuracao.getText());
        task.setNome(txtNome.getText());
        task.setTipo(txtTipo.getText());
        task.setDificuldade(txtDificuldade.getText());
        task.setPrioridade(txtPrioridade.getText());

        if (task.getInicio().isBefore(NOW)) {
            openError();
        } else if (taskList.isEmpty() || taskList.get(taskList.size() - 1).getFim().isBefore(task.getInicio())) {
            taskList.add(task);
        } else {
            int i;
            for (i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getFim().isAfter(task.getInicio())) {
                    taskList.add(i, task);
                    break;
                } else if (taskList.get(i).getFim().isEqual(task.getInicio())) {
                    int j;
                    try {
                        for (j = i; taskList.get(j).getFim().isEqual(task.getInicio()); j++) ;
                        System.out.println(j);
                        taskList.add(j, task);
                    } catch (Exception e) {
                        taskList.add(task);
                    }
                    break;
                }
            }
            if (task.getFim().isAfter(taskList.get(i+1).getInicio())) {
                push(i+1, taskList.get(i+1).getInicio().until(taskList.get(i).getFim(), ChronoUnit.MINUTES));
            }
        }
        refreshTable();
        taskTable.setItems(taskList);
        lblLCM.setText(Integer.toString(Scheduler.calcLCM(taskList)));
        json.gravar(taskList);
        taskTable.setEditable(true);
//        columnNome.setCellValueFactory(TextFieldTableCell.forTableColumn());
//        columnTipo.setCellValueFactory(TextFieldTableCell.forTableColumn());
    }


    @FXML
    private void schedule() {

        styleList = new ArrayList<>(Arrays.asList(style));
        TimelineChart chart = drawChart();
        int width = 800; //Integer.parseInt(lblLCM.getText()) * 55;
        int height = 400; //taskList.size() * 20 * 2 + 100;
        Scene scene  = new Scene(chart, width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }


    private TimelineChart drawChart() {

        List<Task> resultList = new ArrayList<>(Scheduler.schedule(taskList));
        List<String> nameList = new ArrayList<>();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Execution Time");
        xAxis.setAutoRanging(false);
        xAxis.setMinorTickCount(5);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(Double.parseDouble(lblLCM.getText()));
        xAxis.setTickUnit(1);
        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Task ID");
        yAxis.setTickLabelGap(10);
        yAxis.setAutoRanging(false);
        yAxis.setCategories(FXCollections.observableArrayList(nameList));

        final TimelineChart chart = new TimelineChart(xAxis, yAxis);
        chart.setTitle("Rate Monotonic Schedule");
        chart.setLegendVisible(false);

        ObservableList<XYChart.Series<Number, String>> chartData = FXCollections.observableArrayList();
        for(Task t : taskList) {
            ObservableList<XYChart.Data<Number, String>> seriesData = FXCollections.observableArrayList();
            String styleClass = getRandomStyle();
            chartData.add(new XYChart.Series<>(seriesData));
        }
        chart.setData(chartData);
        chart.getStylesheets().add(getClass().getResource("timeline.css").toExternalForm());

        return chart;
    }

    private String getRandomStyle() {
        Random random = new Random();
        int randomIndex = random.nextInt(styleList.size());
        String randomStyle = styleList.get(randomIndex);
        styleList.remove(randomIndex);
        return randomStyle;
    }


    public void onEditChanged(TableColumn.CellEditEvent cellEditEvent) {
        Task task = taskTable.getSelectionModel().getSelectedItem();
        task.setNome((String) cellEditEvent.getNewValue());
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            taskList = FXCollections.observableArrayList(json.ler());
        } catch (Exception ignore) {}
        refreshTable();
        taskTable.setItems(taskList);

//        editableCols();
    }

    private void editableCols() {
//        columnNome.setCellFactory(TextFieldTableCell.forTableColumn());
//        columnNome.getTableView().getRowFactory()
    }


}