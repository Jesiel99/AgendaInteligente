package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class ERRO implements Initializable {

    @FXML
    private javafx.scene.control.Label lblError;

    private static String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblError.setText(errorMessage);
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static void setErrorMessage(String errorMessage) {
        ERRO.errorMessage = errorMessage;
    }
}
