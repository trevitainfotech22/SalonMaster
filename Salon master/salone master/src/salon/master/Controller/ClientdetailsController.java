package salon.master.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import salon.master.GETSET.Adminclient;
import salon.master.connectionprovider.Connectionprovider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ClientdetailsController implements Initializable {

    @FXML
    private Button DELETE;

    @FXML
    private Button EDIT;

    @FXML
    private Label clientname;

    @FXML
    private Label salonname;

    @FXML
    private Label email;

    @FXML
    private Label phone;

    @FXML
    private Label gst;

    @FXML
    private Label password;

    @FXML
    private Label address;

    private Adminclient adminclient;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setAdminclient(Adminclient adminClient) {
        this.adminclient = adminClient;

        clientname.setText(adminClient.getC_name());
        salonname.setText(adminClient.getS_name());
        email.setText(adminClient.getEmail());
        phone.setText(adminClient.getPhone_no());
        gst.setText(adminClient.getGst());
        password.setText(adminClient.getPassword());

        address.setText(adminClient.getAddress());
    }

    @FXML
    public void delete(ActionEvent event) throws SQLException {
        if (adminclient == null) {
            System.out.println("Admin client is null");
            return;
        }

        try (Connection con = Connectionprovider.getConnection()) {
            String query = "DELETE FROM ms_creg WHERE email = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, adminclient.getEmail());
                int deletedRows = pstmt.executeUpdate();
                if (deletedRows > 0) {
                    showAlert("Confirm Logout", "Are you sure you want to logout?", e -> {
                        try {
                            root = FXMLLoader.load(getClass().getResource("../FXML/admindesk.fxml"));
                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        if (adminclient == null) {
            System.out.println("Admin client is null");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ClientRegistration.fxml"));
            root = loader.load();
            ClientregistrationController clientregistrationController = loader.getController();
            clientregistrationController.setUpdate(true);

            clientregistrationController.setTextField(
                    adminclient.getC_name(),
                    adminclient.getS_name(),
                    adminclient.getEmail(),
                    adminclient.getPhone_no(),
                    adminclient.getGst(),
                    adminclient.getPassword(),
                    adminclient.getAddress()
            );

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Dashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/admindesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void showAlert(String title, String content, EventHandler<ActionEvent> onConfirmation) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Set header text to null to hide it
        alert.setContentText(content);

        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                onConfirmation.handle(null);
            }
        });
    }
}
