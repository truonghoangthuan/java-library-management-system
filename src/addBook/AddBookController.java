package addBook;

import database.Database;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    Database database;

    @FXML
    private TextField textFiledbookID;
    @FXML
    private TextField textFieldbookTitle;
    @FXML
    private TextField textFieldbookAuthor;
    @FXML
    private TextField textFieldbookPublisher;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = Database.getDatabase();
    }

    @FXML
    public void buttonHandler(ActionEvent event) {
        if (event.getSource() == buttonSave) {
            addBook();
        } else if (event.getSource() == buttonCancel) {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
    }

    private void addBook() {
        if (textFiledbookID.getText().isEmpty() || textFieldbookTitle.getText().isEmpty() || textFieldbookAuthor.getText().isEmpty() || textFieldbookPublisher.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter in all fields");
            alert.showAndWait();
            return;
        } else {
            String query = "INSERT INTO books VALUES ("
                    + textFiledbookID.getText() + ",'"
                    + textFieldbookTitle.getText() + "','"
                    + textFieldbookAuthor.getText() + "','"
                    + textFieldbookPublisher.getText() + "')";
            database.executeQuery(query);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess");
            alert.setHeaderText("Book added successfully");
            alert.showAndWait();
        }
    }
}
