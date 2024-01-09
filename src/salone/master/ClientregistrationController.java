package salone.master;

import java.io.IOException;
import salone.master.connectionprovider.Connectionprovider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class ClientregistrationController {

    @FXML
    private TextField C_name;

    @FXML
    private TextField c_email;

    @FXML
    private TextField c_gst;

    @FXML
    private PasswordField c_password;

    @FXML
    private TextField c_phone;

    @FXML
    private TextField c_salon;

    @FXML
    private TextArea c_address;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void admindesk(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("./admindesk.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }
    @FXML
    public void submit(ActionEvent event) throws IOException {
        String Clientname = C_name.getText();
        String Salonname = c_salon.getText();
        String Emailid = c_email.getText();
        String Address = c_address.getText();
        String Phonenumber = c_phone.getText();
        String GSTnumber = c_gst.getText();
        String Password = c_password.getText();

        String emailreg = "[^\\s@]+@[^\\s@]+\\.[^\\s@]+";
        String gstreg = "\\d{2}[A-Z]{5}\\d{4}[A-Z]\\d{1}[Z]\\d{1}";

        if (Clientname.isEmpty() || Salonname.isEmpty() || Emailid.isEmpty() || Phonenumber.isEmpty() || GSTnumber.isEmpty() || Password.isEmpty() || Address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill all details");

        } else if (!Emailid.matches(emailreg)) {
            JOptionPane.showMessageDialog(null, "Invalid Email");
        } else if (!Phonenumber.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Invalid Phone Number");
        } else if (Password.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Password cannot contain whitespace");
        } else if (!GSTnumber.matches(gstreg)) {
            JOptionPane.showMessageDialog(null, "Invalid Gstnumber");
        } else {
            try (Connection con = Connectionprovider.getConnection()) {
                String q = "INSERT INTO ms_creg(c_name, s_name, phone_no, email, gst, password, address) VALUES (?, ?, ?, ?, ?, ?,?)"
                        + " ON CONFLICT ON CONSTRAINT uniq_cust_constraint DO NOTHING;";

                try (PreparedStatement pstmt = con.prepareStatement(q)) {
                    pstmt.setString(1, Clientname);
                    pstmt.setString(2, Salonname);
                    pstmt.setString(3, Phonenumber);
                    pstmt.setString(4, Emailid);
                    pstmt.setString(5, GSTnumber);
                    pstmt.setString(6, Password);
                    pstmt.setString(7, Address);

                    int data = pstmt.executeUpdate();

                    if (data > 0) {
                        JOptionPane.showMessageDialog(null, "Client registered successfully");
                        C_name.setText("");
                        c_salon.setText("");
                        c_email.setText("");
                        c_address.setText("");
                        c_phone.setText("");
                        c_gst.setText("");
                        c_password.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Client already exists");
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } catch (SQLException e) {
                System.out.println(e);

            } 
        }
    }
}
