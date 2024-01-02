
package salone.master;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class Loginpage extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("loginpage.fxml"));
        
        Scene scene = new Scene(root);
        
//        text field
        TextField textfield = new TextField();
        textfield.getStyleClass().add("Username");
        stage.setTitle("User Login");
        
        stage.setScene(scene);
        stage.show();
    }

  
    public static void main(String[] args) {
        launch(args);
    }
    
}
