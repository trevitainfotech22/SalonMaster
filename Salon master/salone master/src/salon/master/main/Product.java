package salon.master.main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import salon.master.GETSET.BillingItem;

public class Product extends Application {

    static void setItems(ObservableList<BillingItem> productitemList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/Product.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Salon Master");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
