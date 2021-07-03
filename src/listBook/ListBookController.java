package listBook;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void loadData() {
        database = new Database();
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

                booksList.add(new Book(id, title, author, publisher));
            }
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
        }

        tableBook.getItems().setAll(booksList);
    }

    private void initCol() {
        colID.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
    }
}
