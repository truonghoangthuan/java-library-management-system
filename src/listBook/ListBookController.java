package listBook;

import alert.AlertMaker;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListBookController implements Initializable {
    Database database;

    ObservableList<Book> booksList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Book> tableBook;
    @FXML
    private TableColumn<Book, Integer> colID;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colPublisher;
    @FXML
    private TableColumn<Book, String> colStatus;

    @FXML
    private MenuItem contextMenuDelete;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void loadData() {
        database = Database.getDatabase();
        Connection connection = database.getConnection();
        String query = "SELECT * FROM books";
        ResultSet resultSet;
        Statement statement;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("bookID"));
                String title = resultSet.getString("bookTitle");
                String author = resultSet.getString("bookAuthor");
                String publisher = resultSet.getString("bookPublisher");
                String status = String.valueOf(resultSet.getBoolean("bookStatus"));

                booksList.add(new Book(id, title, author, publisher, status));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        tableBook.setItems(booksList);
    }

    private void initCol() {
        colID.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
        colStatus.setCellValueFactory(new PropertyValueFactory<Book, String>("status"));
    }

    //    Method to handle context menu function
    @FXML
    private void contextMenuHandler(ActionEvent event) {
        if (event.getSource() == contextMenuDelete) {
            Book selectedBook = tableBook.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure want to delete " + selectedBook.getTitle() + "?");

            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                String query = "DELETE FROM books WHERE bookID = " + selectedBook.getId();
                database.executeQuery(query);
                AlertMaker.showSimpleAlert("Success", "Book deleted");
                booksList.remove(selectedBook);
            }
        }
    }
}
