package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public Database() {

    }

    // Connect to the database.
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-library-management-system", "root", "0974667060Th");
            return conn;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Function to execute sql query.
    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
}
