package listMember;

import addBook.AddBookController;
import addMember.AddMemberController;
import alert.AlertMaker;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListMemberController implements Initializable {
    Database database;

    ObservableList<Member> membersList = FXCollections.observableArrayList();

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

    @FXML
    private MenuItem menuItemRefresh;
    @FXML
    private MenuItem menuItemDelete;
    @FXML
    private MenuItem menuItemEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    //    Method to get member data from database.
    private void loadData() {
        membersList.clear();

        database = Database.getDatabase();
        Connection connection = database.getConnection();
        String query = "SELECT * FROM members";
        ResultSet resultSet;
        Statement statement;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("memberID"));
                String name = resultSet.getString("memberName");
                String phone = resultSet.getString("memberPhone");
                String email = resultSet.getString("memberEmail");

                membersList.add(new Member(id, name, phone, email));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        tableMember.setItems(membersList);
    }

    //    Method to render data to table view.
    private void initCol() {
        colID.setCellValueFactory(new PropertyValueFactory<Member, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Member, String>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Member, String>("email"));
    }

    //    Method to check is the booked issued or not.
    private boolean isIssuingBooks(String memberID) {
        String query = "SELECT COUNT(*) FROM issues WHERE memberID = " + memberID;
        Connection connection = database.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return (count > 0);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return false;
    }

    //    Method to handle context menu events.
    @FXML
    private void contextMenuHandler(ActionEvent event) {
        Member selectedMember = tableMember.getSelectionModel().getSelectedItem();
        if (event.getSource() == menuItemRefresh) {
            loadData();
        } else if (event.getSource() == menuItemDelete) {
            if (!isIssuingBooks(selectedMember.getId())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setHeaderText("Are you sure want to delete " + selectedMember.getName() + "?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    String query = "DELETE FROM members WHERE memberID = " + selectedMember.getId();
                    database.executeQuery(query);
                    AlertMaker.showSimpleAlert("Success", "Member deleted");
                    membersList.remove(selectedMember);
                }
            } else {
                AlertMaker.showErrorMessage("Error", selectedMember.getName() + " is issuing books!");
            }
        } else if (event.getSource() == menuItemEdit) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addMember/addMember.fxml"));
                Parent parent = fxmlLoader.load();

                AddMemberController controller = fxmlLoader.getController();
                controller.inflateUI(selectedMember);

                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Edit member");
                stage.setScene(new Scene(parent));
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
