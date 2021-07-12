package main;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    Database database;

    @FXML
    private StackPane rootPane;

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
    private TextField textFieldIssueBookID;
    @FXML
    public ListView issueList;

    Boolean isReadyForSubmission = false;

    @FXML
    private MenuItem menuItemClose;
    @FXML
    private MenuItem menuItemAddBook;
    @FXML
    private MenuItem menuItemAddMember;
    @FXML
    private MenuItem menuItemViewBooks;
    @FXML
    private MenuItem menuItemViewMembers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = Database.getDatabase();
    }

    //    Method to load new window with 2 parameters: location of the fxml file and the title for window
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

    //    Method to handle menu item functions
    @FXML
    public void menuBarHandler(ActionEvent event) {
        if (event.getSource() == menuItemClose)
            ((Stage)rootPane.getScene().getWindow()).close();
        else if (event.getSource() == menuItemAddBook)
            loadAddBook();
        else if (event.getSource() == menuItemAddMember)
            loadAddMember();
        else if (event.getSource() == menuItemViewBooks)
            loadViewBooks();
        else if (event.getSource() == menuItemViewMembers)
            loadViewMembers();
    }

    @FXML
    public void loadAddMember() {
        loadWindow("../addMember/AddMember.fxml", "Add member");
    }

    @FXML
    public void loadViewMembers() {
        loadWindow("../listMember/ListMember.fxml", "Member list");
    }

    @FXML
    public void loadAddBook() {
        loadWindow("../addBook/AddBook.fxml", "Add book");
    }

    @FXML
    public void loadViewBooks() {
        loadWindow("../listBook/ListBook.fxml", "Book list");
    }

    @FXML
    public void loadSettings() {
        loadWindow("../settings/Settings.fxml", "Settings");
    }

    //    Function to handle input data of book information text field
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

    //    Method to handle input data of member information text field
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

    //    Method to handle Issue button
    @FXML
    public void loadIssueFunc() {
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

    //    Method to handle input book information of the text field on submission tab
    @FXML
    public void loadIssueBook() {
        ObservableList<String> issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false;

        String issueBookID = textFieldIssueBookID.getText();
        String query = "SELECT * FROM issues WHERE bookID = " + issueBookID;

        Connection connection = database.getConnection();
        ResultSet resultSet;
        Statement statement;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String bookID = issueBookID;
                String memberID = resultSet.getString("memberID");
                Timestamp issueTime = resultSet.getTimestamp("issueTime");
                int renewCount = resultSet.getInt("renew_count");

                issueData.add("Issue date: " + issueTime.toString());
                issueData.add("Renew count: " + renewCount);

                query = "SELECT * FROM books WHERE bookID = " + bookID;
                ResultSet resultSet1 = statement.executeQuery(query);
                issueData.add("\nBook information: ");
                while (resultSet1.next()) {
                    issueData.add("Book ID: " + resultSet1.getString("bookID"));
                    issueData.add("Book name: " + resultSet1.getString("bookTitle"));
                }

                query = "SELECT * FROM members WHERE memberID = " + memberID;
                resultSet1 = statement.executeQuery(query);
                issueData.add("\nMember information: ");
                while (resultSet1.next()) {
                    issueData.add("Member ID: " + resultSet1.getString("memberID"));
                    issueData.add("Member name: " + resultSet1.getString("memberName"));
                    issueData.add("Member email: " + resultSet1.getString("memberEmail"));
                }
                isReadyForSubmission = true;
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        issueList.getItems().setAll(issueData);
    }

    //    Method to handle submission button
    @FXML
    public void loadSubmissionBook() {
        if (!isReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a book to submit");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure to submit this book!");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            String bookID = textFieldIssueBookID.getText();
            String query = "DELETE FROM issues WHERE bookID = " + bookID;
            database.executeQuery(query);
            String query1 = "UPDATE books SET bookStatus = TRUE WHERE bookID = " + bookID;
            database.executeQuery(query1);

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Success");
            alert1.setHeaderText("Book has been submitted");
            alert1.showAndWait();
        }
    }

    //    Method to handle renew button
    @FXML
    public void loadRenewBook() {
        if (!isReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a book to submit");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure to renew this book!");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            String bookID = textFieldIssueBookID.getText();
            String query = "UPDATE issues SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count + 1 WHERE bookID = " + bookID;
            database.executeQuery(query);

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Success");
            alert1.setHeaderText("Book has been renewed");
            alert1.showAndWait();
        }
    }
}
