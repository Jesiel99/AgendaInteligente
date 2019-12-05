package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Chart implements Initializable {

    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private LineChart<String, Number> lineChart;

    private JSON json;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XYChart.Series<String, Number> line = new XYChart.Series<>();
        line = new XYChart.Series<>();
        json = null;
        try {
            json = new JSON("tarefasEfetuadas");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Tarefa> taskList = json.ler();
        List<LocalDate> days = new ArrayList<LocalDate>();
        List<Integer> taskByDay = new ArrayList<Integer>();

        List<Integer> P1 = new ArrayList<Integer>();
        List<Integer> P2 = new ArrayList<Integer>();
        List<Integer> P3 = new ArrayList<Integer>();

        List<Integer> trivialFacil = new ArrayList<Integer>();
        List<Integer> media = new ArrayList<Integer>();
        List<Integer> dificil = new ArrayList<Integer>();

        for (int j = 0; j < taskList.size(); j++) {
            if (days.contains(taskList.get(j).getFim().toLocalDate())) {
                int index = days.indexOf(taskList.get(j).getFim().toLocalDate());
                taskByDay.set(index, taskByDay.get(index) + 1);
                if (taskList.get(j).getPrioridade() != null && taskList.get(j).getPrioridade().equals("P1")) {
                    P1.set(index, P1.get(index) + 1);
                }
            } else {
                days.add(taskList.get(j).getFim().toLocalDate());
                taskByDay.add(1);
                if (taskList.get(j).getPrioridade() != null && taskList.get(j).getPrioridade().equals("P1")) {
                    P1.add(1);
                } else {
                    P1.add(0);
                }

            }
        }
        XYChart.Series<String, Number> P1Chart = new XYChart.Series<>();
        for (int j = 0; j < days.size(); j++) {
            line.getData().add(new XYChart.Data<String, Number>(String.valueOf(days.get(j)), taskByDay.get(j)));
            P1Chart.getData().add(new XYChart.Data<String, Number>(String.valueOf(days.get(j)), P1.get(j)));
        }
        line.setName("Tarefas");
        P1Chart.setName("P1");
        lineChart.getData().addAll(line);
        lineChart.getData().addAll(P1Chart);


    }


}
