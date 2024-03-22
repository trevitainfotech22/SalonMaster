package salon.master.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import salon.master.connectionprovider.Connectionprovider;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;

public class BookAppointmentController implements Initializable {

    Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    @FXML
    private TextField NAME;

    @FXML
    private TextField PHONE;

    @FXML
    private DatePicker DATE;

    @FXML
    private TextField searchappointment;

    @FXML
    private Button APPBOOKING;

    @FXML
    private Label userNotFoundLabel;

    @FXML
    private ComboBox<String> hours;

    @FXML
    private ComboBox<String> minute;

    @FXML
    private ComboBox<String> AP;

    @FXML
    private Button REGBOOK;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane Slider;
    
    @FXML
    private ScrollPane scroll;

    String datergx = "^\\d{1,2}-\\d{1,2}-\\d{4}$";

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void APPBOOKING(ActionEvent event) {
        String name = NAME.getText();
        String phone_no = PHONE.getText();
        LocalDate selectedDate = DATE.getValue();
        String selectedHour = hours.getValue();
        String selectedMinute = minute.getValue();
        String selectedAMPM = AP.getValue();

        // Check if any field is empty
        if (name.isEmpty() || phone_no.isEmpty() || selectedDate == null || selectedHour == null || selectedMinute == null || selectedAMPM == null) {
            JOptionPane.showMessageDialog(null, "All fields must be required");
            return; // Exit the method without booking the appointment
        }

        // Validate phone number format
        if (!phone_no.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Invalid Phone Number");
            return; // Exit the method without booking the appointment
        }

        // Validate date format
        if (!selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).matches(datergx)) {
            JOptionPane.showMessageDialog(null, "Use this Date format: dd-mm-yyyy");
            return; // Exit the method without booking the appointment
        }

        // At this point, all fields are filled and validated
        // Proceed to book the appointment
        con = Connectionprovider.getConnection();

        String checkphone = "SELECT id FROM ms_user WHERE phone_no=?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkphone)) {
            checkStmt.setString(1, phone_no);
            ResultSet userResultSet = checkStmt.executeQuery();

            if (userResultSet.next()) {
                int userid = userResultSet.getInt("id");

                String time = formatTime();

                String apbquery = "insert into ms_apb (user_id,date,time) values (?,?,?)";
                try (var apbstatement = con.prepareStatement(apbquery)) {
                    apbstatement.setInt(1, userid);
                    apbstatement.setDate(2, java.sql.Date.valueOf(selectedDate));
                    apbstatement.setString(3, time);

                    apbstatement.executeUpdate();

                    NAME.setText("");
                    PHONE.setText("");
                    DATE.setValue(null);

                    root = FXMLLoader.load(getClass().getResource("../FXML/Appointmentdesk.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    JOptionPane.showMessageDialog(null, "Appointment booked successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not registered. Please register first.");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void REGBOOK(ActionEvent event) {

        String name = NAME.getText();
        String phone_no = PHONE.getText();
        LocalDate selectedDate = DATE.getValue();
        String selectedHour = hours.getValue();
        String selectedMinute = minute.getValue();
        String selectedAMPM = AP.getValue();

        if (name.isEmpty() || phone_no.isEmpty() || selectedDate == null || selectedHour == null || selectedMinute == null || selectedAMPM == null) {
            JOptionPane.showMessageDialog(null, "All fields must be required");
            return; // Exit the method without booking the appointment
        }

        // Validate phone number format
        if (!phone_no.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Invalid Phone Number");
            return; // Exit the method without booking the appointment
        }

        // Validate date format
        if (!selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).matches(datergx)) {
            JOptionPane.showMessageDialog(null, "Use this Date format: dd-mm-yyyy");
            return; // Exit the method without booking the appointment
        }

        String checkphone = "SELECT id FROM ms_user WHERE phone_no=?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkphone)) {
            checkStmt.setString(1, phone_no);
            ResultSet userResultSet = checkStmt.executeQuery();

            if (userResultSet.next()) {
                JOptionPane.showMessageDialog(null, "Customer already exists");
            } else {
//                 int userid = userResultSet.getInt("id");
                try {
                    String userquery = "INSERT INTO ms_user (name, phone_no) VALUES (?,?) RETURNING id";
                    try (PreparedStatement userstmt = con.prepareStatement(userquery)) {
                        userstmt.setString(1, name);
                        userstmt.setString(2, phone_no);

                        try (ResultSet userrs = userstmt.executeQuery()) {
                            if (userrs.next()) {
                                int userid = userrs.getInt(1);

                                String time = formatTime();

                                String regbokquery = "BEGIN;"
                                        + "insert into ms_cureg (userid) values (?);"
                                        + "insert into ms_apb (user_id,date,time) values (?,?,?);"
                                        + "COMMIT;";
                                try (PreparedStatement ABstmt = con.prepareStatement(regbokquery)) {
                                    ABstmt.setInt(1, userid);
                                    ABstmt.setInt(2, userid);
                                    ABstmt.setDate(3, java.sql.Date.valueOf(selectedDate));
                                    ABstmt.setString(4, time);

                                    ABstmt.executeUpdate();
                                    root = FXMLLoader.load(getClass().getResource("../FXML/Appointmentdesk.fxml"));
                                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    JOptionPane.showMessageDialog(null, "Registerd And Book successfully");

                                }

                            }
                        }
                    }

                } catch (SQLException e) {
                    System.out.println(e);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //    Time string format
    private String formatTime() {
        // Get the selected values from combo boxes
        String selectedHour = hours.getValue();
        String selectedMinute = minute.getValue();
        String selectedAMPM = AP.getValue();

        // Format the hour to ensure it's in two-digit format (e.g., 01 instead of 1)
        String formattedHour = (selectedHour);
        return formattedHour + ":" + (selectedMinute) + " " + selectedAMPM;
    }

//    @FXML
//    public void reg_book(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("../FXML/Customerregistration.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {


//        horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//      Slider animation

        scroll.setTranslateX(-800);
        menuback.setTranslateX(-800);

        menu.setOnMouseClicked(event -> {
            TranslateTransition slideMenu = new TranslateTransition(); // Transition for the menu
            slideMenu.setDuration(Duration.seconds(0.4));
            slideMenu.setNode(scroll); // Set the menu as the node to animate
            slideMenu.setToX(0);

            TranslateTransition slideBack = new TranslateTransition(); 
            slideBack.setDuration(Duration.seconds(0.4));
            slideBack.setNode(menuback); 
            slideBack.setToX(0);

            ParallelTransition parallelTransition = new ParallelTransition(slideMenu, slideBack); 
            parallelTransition.play();

            parallelTransition.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(event -> {
            TranslateTransition slideMenu = new TranslateTransition(); 
            slideMenu.setDuration(Duration.seconds(0.4));
            slideMenu.setNode(scroll); 
            slideMenu.setToX(-800);

            TranslateTransition slideBack = new TranslateTransition();
            slideBack.setDuration(Duration.seconds(0.4));
            slideBack.setNode(menuback); 
            slideBack.setToX(-800);

            ParallelTransition parallelTransition = new ParallelTransition(slideMenu, slideBack);
            parallelTransition.play();

            parallelTransition.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });
//        -------------------------------------------------------

        searchappointment.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String query = "SELECT name, phone_no FROM ms_user WHERE phone_no = ?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, newValue);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        NAME.setText(rs.getString("name"));
                        PHONE.setText(rs.getString("phone_no"));
                        userNotFoundLabel.setVisible(false);
                    } else {
                        NAME.setText("");
                        PHONE.setText("");
                        userNotFoundLabel.setVisible(true);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                NAME.setText("");
                PHONE.setText("");
                userNotFoundLabel.setVisible(false);
            }
        });

        try {
            con = Connectionprovider.getConnection();

            String searchquery = "SELECT phone_no FROM ms_user;";
            pstmt = con.prepareStatement(searchquery);
            rs = pstmt.executeQuery();
            ObservableList<String> searchlist = FXCollections.observableArrayList();

            while (rs.next()) {
                searchlist.add(rs.getString("phone_no"));
            }

            TextFields.bindAutoCompletion(searchappointment, searchlist);
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Disable past dates in the date picker
        DATE.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        FillcomboBox();
    }

    private void FillcomboBox() {
        AP.setItems(FXCollections.observableArrayList("AM", "PM"));

        ObservableList<String> hoursliList = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            hoursliList.add(String.format("%02d", i));
        }
        hours.setItems(hoursliList);

        ObservableList<String> minutelist = FXCollections.observableArrayList();
        for (int j = 0; j <= 59; j++) {
            minutelist.add(String.format("%02d", j));
        }
        minute.setItems(minutelist);

    }
//    Redirect pages

    @FXML
    public void Dashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Appointmentdesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Im(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Product.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Report(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Report.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Reward(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Reward.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Setting(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Setting.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void apb_list(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/APPOINMENTS.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void add_product(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Inventory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void billing(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Billing.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void cu_list(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Customerlist.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void cu_reg(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Customerregistration.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
     @FXML
    public void emp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/EmployeeList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void blhistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Billinghistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
