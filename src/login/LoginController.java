package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import settings.Preferences;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonCancel;

    Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preferences = Preferences.getPreferences();
    }

    @FXML
    public void buttonHandler(ActionEvent event) {
        if (event.getSource() == buttonLogin) {
            String username = textFieldUsername.getText();
            String password = DigestUtils.sha1Hex(passwordField.getText());

            if (username.equals(preferences.getUsername()) && password.equals(preferences.getPassword())) {
                ((Stage)rootPane.getScene().getWindow()).close();
                loadWindow("../main/Main.fxml", "Library Management System");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong username or password!");
                alert.showAndWait();
            }
        }
        if (event.getSource() == buttonCancel) {
            System.exit(0);
        }
    }

    void loadWindow(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
