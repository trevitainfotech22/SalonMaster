package salone.master;

import java.sql.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import salone.master.connectionprovider.Connectionprovider;

public class AdmindeskController implements Initializable {

    @FXML
    private TableColumn<Adminclient, Integer> id;

    @FXML
    private TableColumn<Adminclient, String> c_name;

    @FXML
    private TableColumn<Adminclient, String> s_name;

    @FXML
    private TableColumn<Adminclient, String> address;

    @FXML
    private TableColumn<Adminclient, String> phone_no;

    @FXML
    private TableColumn<Adminclient, String> email;

    @FXML
    private TableColumn<Adminclient, String> gst;

    @FXML
    private TableColumn<Adminclient, String> password;

    @FXML
    private TableView<Adminclient> myTable;

    @FXML
    private Label countfeild;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Integer count;

    @FXML
    public void AddToClient(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("./Clientregistration.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("./AdminLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    ObservableList<Adminclient> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        c_name.setCellValueFactory(new PropertyValueFactory<>("c_name"));
        s_name.setCellValueFactory(new PropertyValueFactory<>("s_name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        phone_no.setCellValueFactory(new PropertyValueFactory<>("phone_no"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        gst.setCellValueFactory(new PropertyValueFactory<>("gst"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        try {
            Connection con = Connectionprovider.getConnection();
            String q = "SELECT * FROM ms_creg;";
            PreparedStatement pstm = con.prepareStatement(q);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                int dbid = rs.getInt("id");
                String dbc_name = rs.getString("c_name");
                String dbs_name = rs.getString("s_name");
                String dbaddress = rs.getString("address");
                String dbphone_no = rs.getString("phone_no");
                String dbemail = rs.getString("email");
                String dbgst = rs.getString("gst");
                String dbpassword = rs.getString("password");

                Adminclient adminClient = new Adminclient(dbid, dbc_name, dbs_name, dbaddress, dbphone_no, dbemail, dbgst, dbpassword);
                list.add(adminClient);
            }

            myTable.setItems(list);

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
    }
}
