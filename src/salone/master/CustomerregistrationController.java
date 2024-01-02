package salone.master;

import salone.master.connectionprovider.Connectionprovider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
    private Button mybutton;
    
    

    @FXML
    private void submit(ActionEvent event) {
        String name = cnameTextField.getText();
        String phone_no = numTextField.getText();
        String birthDateString = BdateTextField.getText();
        String anniversaryDateString = AdateTextField.getText();
        String gen = GenTextFIeld.getText();

        Date birthDate = null;
        Date anniversaryDate = null;

        if (name.isEmpty() || phone_no.isEmpty() || birthDateString.isEmpty() || anniversaryDateString.isEmpty() || gen.isEmpty()) {
            System.out.println("All fields must be filled");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            try {
                birthDate = dateFormat.parse(birthDateString);
                anniversaryDate = dateFormat.parse(anniversaryDateString);
                
                
                try{
                    Class.forName("org.postgresql.Driver");
                    
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
                                    custatement.setDate(2, new java.sql.Date(birthDate.getTime()));
                                    custatement.setDate(3, new java.sql.Date(anniversaryDate.getTime()));
                                    custatement.setString(4, gen);
                                    
                                    custatement.executeUpdate();
                                }
                            }
                        }
                        System.out.print("Inserted....");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                
            } catch (ParseException e) {
                System.out.println("Error parsing dates");
                e.printStackTrace();
            }
        }

    }
}
