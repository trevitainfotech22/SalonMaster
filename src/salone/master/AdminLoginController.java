package salone.master;

import java.io.IOException;
import salone.master.connectionprovider.Connectionprovider;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class AdminLoginController implements Initializable {

    @FXML
    private TextField UnameTextField;

    @FXML
    private PasswordField myPasswordField;

    PreparedStatement pst;
    ResultSet rs;

    @FXML
    public void submit(ActionEvent event) throws IOException {
        Parent root;

        String Uname = UnameTextField.getText();
        String Pass = myPasswordField.getText();

        if (Uname.isEmpty() || Pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username or password blank");
        } else {
            try {
                Class.forName("org.postgresql.Driver");

                Connection con = Connectionprovider.getConnection();

                pst = con.prepareStatement("select * from admin where name=? and password=?");

                pst.setString(1, Uname);
                pst.setString(2, Pass);

                rs = pst.executeQuery();

                if (rs.next()) {
                    root = FXMLLoader.load(getClass().getResource("./admindesk.fxml"));
                    Stage primarystage = new Stage();
 //admin                   Parent root = FXMLLoader.load(getClass().getResource("admindesk.fxml"));
                    Scene scene = new Scene(root);
                    primarystage.setTitle("Admin Login");
                    primarystage.setScene(scene);
                    primarystage.show();
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed");
                    UnameTextField.setText("");
                    myPasswordField.setText("");
                    UnameTextField.requestFocus();
                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(AdminLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
