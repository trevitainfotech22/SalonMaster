package salon.master.main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import salon.master.Controller.AdminLoginController.session;

public class Admindesk extends Application {

   
      @Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("../FXML/admindesk.fxml"));


        Scene scene = new Scene(root);
        stage.setTitle("Salon Master");      
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



