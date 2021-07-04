package listMember;

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

public class ListMemberController implements Initializable {
    Database database;

    ObservableList<Member> booksList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Member> tableMember;
    @FXML
    private TableColumn<Member, Integer> colID;
    @FXML
    private TableColumn<Member, String> colName;
    @FXML
    private TableColumn<Member, String> colPhone;
    @FXML
    private TableColumn<Member, String> colEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void loadData() {
        database = new Database();
        Connection connection = database.getConnection();
        String query = "SELECT * FROM members";
        ResultSet resultSet;
        Statement statement;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("memberID"));
                String title = resultSet.getString("memberName");
                String author = resultSet.getString("memberPhone");
                String publisher = resultSet.getString("memberEmail");

                booksList.add(new Member(id, title, author, publisher));
            }
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
        }

        tableMember.getItems().setAll(booksList);
    }

    private void initCol() {
        colID.setCellValueFactory(new PropertyValueFactory<Member, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Member, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Member, String>("email"));
    }
}
