package salone.master;

import java.io.IOException;
import salone.master.connectionprovider.Connectionprovider;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void submit(ActionEvent event) throws IOException {
        String Uname = UnameTextField.getText();
        String Pass = myPasswordField.getText();

        if (Uname.isEmpty() || Pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username or password blank");
        } else {
            try {
                Connection con = Connectionprovider.getConnection();

                pst = con.prepareStatement("select * from ms_adminuser where username=? and password=?");

                pst.setString(1, Uname);
                pst.setString(2, Pass);

                rs = pst.executeQuery();

                if (rs.next()) {
                    root = FXMLLoader.load(getClass().getResource("./admindesk.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed");
                    UnameTextField.setText("");
                    myPasswordField.setText("");
                    UnameTextField.requestFocus();
                }

            } catch (SQLException ex) {
                Logger.getLogger(AdminLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

    }
}
