package salon.master.main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Appointmentdesk extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/Appointmentdesk.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Salon Master");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
