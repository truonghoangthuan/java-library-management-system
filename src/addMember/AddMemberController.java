package addMember;

import alert.AlertMaker;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import listBook.Book;
import listMember.Member;

import java.net.URL;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {
    Database database;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField textFieldID;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPhone;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;

    private Boolean isEditable = Boolean.FALSE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = Database.getDatabase();
    }

    public void buttonHandler(ActionEvent event) {
        if (event.getSource() == buttonSave) {
            addMember();
        } else if (event.getSource() == buttonCancel) {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
    }

    //    Method to execute query add book to database.
    private void addMember() {
        if (textFieldID.getText().isEmpty() || textFieldName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPhone.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter in all fields");
            alert.showAndWait();
            return;
        } else if (isEditable) {
            editMemberHandler();
        } else {
            String query = "INSERT INTO members VALUES ("
                    + textFieldID.getText() + ",'"
                    + textFieldName.getText() + "','"
                    + textFieldPhone.getText() + "','"
                    + textFieldEmail.getText() + "')";
            database.executeQuery(query);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess");
            alert.setHeaderText("Member added successfully");
            alert.showAndWait();
        }
    }

    //    Method to render selected member data to the edit form.
    public void inflateUI(Member member) {
        isEditable = Boolean.TRUE;

        textFieldID.setText(member.getId());
        textFieldName.setText(member.getName());
        textFieldPhone.setText(member.getPhone());
        textFieldEmail.setText(member.getEmail());

        textFieldID.setEditable(false);
    }

    //    Method to execute edit member function.
    private void editMemberHandler() {
        String query = "UPDATE members SET "
                + "memberName = " + "'" + textFieldName.getText() + "', "
                + "memberPhone = " + "'" + textFieldPhone.getText() + "', "
                + "memberEmail = " + "'" + textFieldEmail.getText() + "' "
                + "WHERE memberID = " + textFieldID.getText();
        database.executeQuery(query);
        AlertMaker.showSimpleAlert("Success", "Member edited");
    }
}
