package addBook;

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

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    Database database;

    @FXML
    private TextField textFiledBookID;
    @FXML
    private TextField textFieldBookTitle;
    @FXML
    private TextField textFieldBookAuthor;
    @FXML
    private TextField textFieldBookPublisher;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private AnchorPane rootPane;

    private Boolean isEditable = Boolean.FALSE;

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

    //    Method to execute query add book to database.
    private void addBook() {
        if (textFiledBookID.getText().isEmpty() || textFieldBookTitle.getText().isEmpty() || textFieldBookAuthor.getText().isEmpty() || textFieldBookPublisher.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter in all fields");
            alert.showAndWait();
            return;
        } else if (isEditable) {
            editBookHandler();
        } else {
            String query = "INSERT INTO books VALUES ("
                    + textFiledBookID.getText() + ",'"
                    + textFieldBookTitle.getText() + "','"
                    + textFieldBookAuthor.getText() + "','"
                    + textFieldBookPublisher.getText() + "',"
                    + true + ")";
            database.executeQuery(query);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess");
            alert.setHeaderText("Book added successfully");
            alert.showAndWait();
        }
    }

    //    Method to render selected book data to the edit form.
    public void inflateUI(Book book) {
        isEditable = Boolean.TRUE;

        textFiledBookID.setText(book.getId());
        textFieldBookTitle.setText(book.getTitle());
        textFieldBookAuthor.setText(book.getAuthor());
        textFieldBookPublisher.setText(book.getPublisher());

        textFiledBookID.setEditable(false);
    }

    //    Method to execute edit book function.
    private void editBookHandler() {
        String query = "UPDATE books SET "
                + "bookTitle = " + "'" + textFieldBookTitle.getText() + "', "
                + "bookAuthor = " + "'" + textFieldBookAuthor.getText() + "', "
                + "bookPublisher = " + "'" + textFieldBookPublisher.getText() + "' "
                + "WHERE bookID = " + textFiledBookID.getText();
        database.executeQuery(query);
        AlertMaker.showSimpleAlert("Success", "Book edited");
    }
}
