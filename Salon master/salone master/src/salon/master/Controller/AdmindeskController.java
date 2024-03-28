package salon.master.Controller;


import salon.master.GETSET.Adminclient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.util.Duration;
import salon.master.connectionprovider.Connectionprovider;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
public class AdmindeskController implements Initializable {

    @FXML
    private TableColumn<Adminclient, String> c_name;

    @FXML
    private TableColumn<Adminclient, String> email;

    @FXML
    private TableView<Adminclient> myTable;

    @FXML
    private TextField adminsearchbar;

    private FilteredList<Adminclient> filteredData;

    @FXML
    private Label countfeild;

    @FXML
    private StackPane Slider;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Integer count;
    
     private Adminclient adminclient;
    @FXML
    public void deleteButton(ActionEvent event) throws SQLException {
        Adminclient adminClient = myTable.getSelectionModel().getSelectedItem();
        if (adminClient == null) {
            Showalert();
            return;
        }

        showAlert("Confirm Delete", "Are you sure you want to delete this item?", e -> {
            try {
                String query = "DELETE FROM ms_creg WHERE email = ?";
                Connection connection = Connectionprovider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, adminClient.getEmail());
                preparedStatement.execute();

                list.remove(adminClient);
                count--;
                countfeild.setText(String.valueOf(count));
                myTable.refresh();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

    @FXML
public void showdetails(ActionEvent event) {
    Adminclient adminclient = myTable.getSelectionModel().getSelectedItem();
    if (adminclient == null) {
        System.out.println("Admin client is null");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Clientdetails.fxml"));
        root = loader.load();
        ClientdetailsController clientdetailsController = loader.getController();
        clientdetailsController.setAdminclient(adminclient); 

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        System.out.println(e);
    }
}


    @FXML
    public void AddToClient(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Clientregistration.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        showAlert("Confirm Logout", "Are you sure you want to logout?", e -> {
            try {
                root = FXMLLoader.load(getClass().getResource("../FXML/AdminLogin.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });
    }

    ObservableList<Adminclient> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        Slider.setVisible(false);
        c_name.setCellValueFactory(new PropertyValueFactory<>("c_name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));


        try {
            Connection con = Connectionprovider.getConnection();
            String q = "SELECT * FROM ms_creg;";
            PreparedStatement pstm = con.prepareStatement(q);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                String dbc_name = rs.getString("c_name");
                String dbs_name = rs.getString("s_name");
                String dbemail = rs.getString("email");
                String dbphone_no = rs.getString("phone_no");
                String dbgst = rs.getString("gst");
                String dbpassword = rs.getString("password");
                String dbaddress = rs.getString("address");

                Adminclient adminClient = new Adminclient(dbc_name, dbs_name, dbemail, dbphone_no, dbgst, dbpassword, dbaddress);
                list.add(adminClient);
            }

            filteredData = new FilteredList<>(list, b -> true);
            adminsearchbar.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Adminclient -> {

                    if (newValue == null || newValue.isBlank()) {
                        return true;
                    }
                    String searchkeyword = newValue.toLowerCase();

                    if (Adminclient.getEmail().toLowerCase().contains(searchkeyword)) {
                        return true;
                    } else if (Adminclient.getC_name().toLowerCase().contains(searchkeyword)) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });

            SortedList<Adminclient> sorteData = new SortedList<>(filteredData);
            sorteData.comparatorProperty().bind(myTable.comparatorProperty());

            myTable.setItems(sorteData);
            myTable.refresh();

            try {
                pstm = con.prepareStatement("SELECT COUNT(*) As clientcount FROM ms_creg");
                rs = pstm.executeQuery();
                while (rs.next()) {
                    count = rs.getInt("clientcount");
                }
                countfeild.setText(String.valueOf(count));
            } catch (SQLException e) {
                System.err.println(e);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        Slider.setTranslateY(600);
        myTable.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(Slider);
            slide.setToY(0);
            slide.play();

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.4), Slider);
                slideOut.setToY(600);
                slideOut.play();
            });
            pause.play();
        });
    }

    private void showAlert(String title, String content, EventHandler<ActionEvent> onConfirmation) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                onConfirmation.handle(null);
            }
        });
    }

    private void Showalert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Value Entered");
        alert.setHeaderText(null);
        alert.setContentText("Please select data from table.");
        alert.showAndWait();
    }
}
