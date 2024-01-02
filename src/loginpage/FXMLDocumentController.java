package loginpage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class FXMLDocumentController {

   

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    

    
    private void submit(ActionEvent event) {
        String username = usernameField.getText();
        System.out.println(username);

        String password = passwordField.getText();
        System.out.println(password);
        
        
        
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

//    
    
}
