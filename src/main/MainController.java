package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController {
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
