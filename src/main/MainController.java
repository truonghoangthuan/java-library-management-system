package main;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    Database database;

    @FXML
    private Text bookTitle;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private TextField textFieldBookInfor;

    @FXML
    private Text memberName;
    @FXML
    private Text memberContact;
    @FXML
    private TextField textFieldMemberInfor;

    @FXML
    public void loadAddMember(ActionEvent event) {
        loadWindow("../addMember/AddMember.fxml", "Add member");
    }

    @FXML
    public void loadViewMembers(ActionEvent event) {
        loadWindow("../listMember/ListMember.fxml", "Member list");
    }

    @FXML
    public void loadAddBook(ActionEvent event) {
        loadWindow("../addBook/AddBook.fxml", "Add book");
    }

    @FXML
    public void loadViewBooks(ActionEvent event) {
        loadWindow("../listBook/ListBook.fxml", "Book list");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = Database.getDatabase();
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

    @FXML
    public void loadBookInfor() {
        String id = textFieldBookInfor.getText();
        String query = "SELECT * FROM books WHERE bookID=" + id;
        Connection connection = database.getConnection();
        Statement statement;
        ResultSet resultSet;
        Boolean flag = false;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("bookTitle");
                String author = resultSet.getString("bookAuthor");
                Boolean status = resultSet.getBoolean("bookStatus");

                bookTitle.setText(name);
                bookAuthor.setText(author);
                String statusText = (status) ? "Available" : "Not available";
                bookStatus.setText(statusText);

                flag = true;
            }
            if (!flag) {
                bookTitle.setText("Can't find book!");
                bookAuthor.setText("");
                bookStatus.setText("");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    @FXML
    public void loadMemberInfor() {
        String id = textFieldMemberInfor.getText();
        String query = "SELECT * FROM members WHERE memberID=" + id;
        Connection connection = database.getConnection();
        Statement statement;
        ResultSet resultSet;
        Boolean flag = false;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("memberName");
                String email = resultSet.getString("memberEmail");

                memberName.setText(name);
                memberContact.setText(email);

                flag = true;
            }
            if (!flag) {
                memberName.setText("Can't find member!");
                memberContact.setText("");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    @FXML
    public void loadIssueFunc(ActionEvent event) {
        String bookId = textFieldBookInfor.getText();
        String memberID = textFieldMemberInfor.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm issue");
        alert.setHeaderText("Confirm issue book " + bookTitle.getText() + " to " + memberName.getText());

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            String query1 = "INSERT INTO issues(bookID, memberID) VALUES ("
                    + "'" + bookId + "',"
                    + "'" + memberID + "')";
            database.executeQuery(query1);

            String query2 = "UPDATE books SET bookStatus = false WHERE bookID=" + bookId;
            database.executeQuery(query2);

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Sucess");
            alert2.setHeaderText("Book issued successfully");
            alert2.showAndWait();
        }
    }
}
