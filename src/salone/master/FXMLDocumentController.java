package salone.master;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import salone.master.connectionprovider.Connectionprovider;

public class FXMLDocumentController {

    @FXML
    private Button mybutton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Connection con;
    private String registeredUser;
    private String registeredPassword;

    public void setRegisteredUser(String registeredUser, String registeredPassword) {
        this.registeredUser = registeredUser;
        this.registeredPassword = registeredPassword;
    }

    @FXML
    private void submit(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
        } else {
            try {
                Class.forName("org.postgresql.Driver");

                con = Connectionprovider.getConnection();

                String q = "SELECT * FROM ms_creg WHERE c_name = ? AND password = ?";
                PreparedStatement pstmt = con.prepareStatement(q);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    showAlert("Success", "Login successful");
                } else {
                    showAlert("Error", "Invalid credentials. Please try again.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null && !con.isClosed()) {
                        con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
