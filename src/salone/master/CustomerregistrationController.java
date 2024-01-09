package salone.master;

import salone.master.connectionprovider.Connectionprovider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CustomerregistrationController {
    
    Connection con;
    

    @FXML
    private TextField cnameTextField;

    @FXML
    private TextField numTextField;

    @FXML
    private TextField BdateTextField;

    @FXML
    private TextField AdateTextField;

    @FXML
    private TextField GenTextFIeld;

  
    
    

    @FXML
    public void submit(ActionEvent event) {
        String name = cnameTextField.getText();
        String phone_no = numTextField.getText();
        String birthDate = BdateTextField.getText();
        String anniversaryDate = AdateTextField.getText();
        String gen = GenTextFIeld.getText();

        
        String Birtregx = "^\\d{1,2}-\\d{1,2}-\\d{4}$";

        if (name.isEmpty() || phone_no.isEmpty() || birthDate.isEmpty() || anniversaryDate.isEmpty() || gen.isEmpty()) {
           JOptionPane.showMessageDialog(null, "All fields must be required");
        }
        else if(!phone_no.matches("\\d{10}")){
                   JOptionPane.showMessageDialog(null, "Invalid Phone Number");
        }
        else if(!birthDate.matches(Birtregx)){
            JOptionPane.showMessageDialog(null, "use this Dateformate:dd-mm-yyyy");
        }
        else if(!anniversaryDate.matches(Birtregx)){
            JOptionPane.showMessageDialog(null, "use this Dateformate:dd-mm-yyyy");
        }
        else {
                
                try{
                    
                   con = Connectionprovider.getConnection();
                    
                    String q = "Insert into ms_user (name,phone_no) values (?,?) RETURNING id";
                    try(PreparedStatement userstatement = con.prepareStatement(q)){
                        userstatement.setString(1, name);
                        userstatement.setString(2,phone_no);
                        
                        try(var rs = userstatement.executeQuery()){
                            if(rs.next()){
                                int userid = rs.getInt(1);
                                
                                String cuquery = "insert into ms_cureg (userid,c_birthday,c_anniversary, gender) values (?,?,?,?)";
                                try(var custatement = con.prepareStatement(cuquery)){
                                    custatement.setInt(1, userid);
                                    custatement.setString(2, birthDate);
                                    custatement.setString(3, anniversaryDate);
                                    custatement.setString(4, gen);
                                    
                                    custatement.executeUpdate();
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Registration successfully");
                    }
                }catch(SQLException e){
                    System.out.println(e);
                    }
                
                
           
        }

    }
}
