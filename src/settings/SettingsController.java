package settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField textFieldDaysWithoutFine;
    @FXML
    private TextField textFieldFinePerDay;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDefaultValue();
    }

    private void initDefaultValue() {
        Preferences preferences = Preferences.getPreferences();
        textFieldDaysWithoutFine.setText(String.valueOf(preferences.getNumberOfDaysWithoutFine()));
        textFieldFinePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        textFieldUsername.setText(preferences.getUsername());
        passwordField.setText(preferences.getPassword());
    }

    @FXML
    private void buttonHandler(ActionEvent event) {
        if (event.getSource() == buttonSave) {
            Preferences preferences = Preferences.getPreferences();

            preferences.setNumberOfDaysWithoutFine(Integer.parseInt(textFieldDaysWithoutFine.getText()));
            preferences.setFinePerDay(Float.parseFloat(textFieldFinePerDay.getText()));
            preferences.setUsername(textFieldUsername.getText());
            preferences.setPassword(passwordField.getText());

            Preferences.writePrferencesToFile(preferences);
        }
        if (event.getSource() == buttonCancel) {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
    }
}
