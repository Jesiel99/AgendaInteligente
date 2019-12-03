package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfiguracoesController implements Initializable {

    @FXML
    TextField txtInicio, txtFim;
    @FXML
    Button btnReturn, btnAdd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
