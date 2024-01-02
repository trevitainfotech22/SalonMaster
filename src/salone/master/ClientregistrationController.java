package salone.master;

import java.io.IOException;
import salone.master.connectionprovider.Connectionprovider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.TextArea;



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
    
    Connection con;

    @FXML
    public void submit(ActionEvent event) throws IOException{
    String Clientname = C_name.getText();
    String Salonname = c_salon.getText();
    String Emailid = c_email.getText();
    String Address = c_address.getText();
    String Phonenumber = c_phone.getText();
    String GSTnumber = c_gst.getText();
    String Password = c_password.getText();
    
    if (Clientname.isEmpty() || Salonname.isEmpty() || Emailid.isEmpty() || Phonenumber.isEmpty() || GSTnumber.isEmpty() || Password.isEmpty() || Address.isEmpty() ) {
        System.out.print("fill all details");
    } else {
        try{
            
            Class.forName("org.postgresql.Driver");
            
            con = Connectionprovider.getConnection();
            
            String q = "insert into ms_creg(c_name,s_name,phone_no,email,gst,password,address) values (?,?,?,?,?,?,?)";
            
            PreparedStatement pstmt = con.prepareStatement(q);
            pstmt.setString(1, Clientname);
            pstmt.setString(2, Salonname);
            pstmt.setString(3, Emailid);
            pstmt.setString(4, Phonenumber);
            pstmt.setString(5, GSTnumber);
            pstmt.setString(6, Password);
            pstmt.setString(7, Address);
            
            pstmt.executeUpdate();
            
            System.out.print("inserted...");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    }

}
