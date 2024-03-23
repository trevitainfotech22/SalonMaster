package salon.master.Controller;

import java.io.IOException;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import salon.master.connectionprovider.Connectionprovider;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import salon.master.GETSET.BillEntry;

public class BillinghistoryController implements Initializable {

    @FXML
    public TextField searchbar;

    @FXML
    private Button search;

    @FXML
    private TableView<BillEntry> Billinghistory;

    @FXML
    private TableColumn<BillEntry, String> invoiceNoColumn;

    @FXML
    private TableColumn<BillEntry, String> custNumColumn;

    @FXML
    private TableColumn<BillEntry, String> itemColumn;

    @FXML
    private TableColumn<BillEntry, String> paymentMethodColumn;

    @FXML
    private TableColumn<BillEntry, String> dateColumn;

    @FXML
    private TableColumn<BillEntry, Integer> stockColumn;

    @FXML
    private TableColumn<BillEntry, Double> priceColumn;

    private Connection con;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private Text month;

    @FXML
    private StackPane navSlider;

    @FXML
    private StackPane Slider;

    @FXML
    private Button EDIT;

    @FXML
    private Button DELETE;

    @FXML
    private ScrollPane scroll;

    private Stage stage;
    private Scene scene;
    private Parent root;

 

    private ObservableList<BillEntry> billEntries = FXCollections.observableArrayList();

    @FXML
    public void delete(ActionEvent event) {

    }

    @FXML
    public void edit(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Slider.setTranslateY(600);
        Billinghistory.setOnMouseClicked(event -> {
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

        //        horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setTranslateX(-800);
        menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(scroll);

            slide.setToX(0);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(scroll);

            slide.setToX(-800);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });

        //        Search bar and date bar
        search.setOnMouseClicked(event -> {
    searchbar.setVisible(!searchbar.isVisible());
});

        con = Connectionprovider.getConnection();

        if (con == null) {
            System.err.println("Error: Unable to establish connection to the database.");
            return;
        }

        initializeColumns();
        fetchDataFromDatabase();
        setupSearchFilter();
    }

    private void initializeColumns() {
        invoiceNoColumn.setCellValueFactory(cellData -> cellData.getValue().invoiceNoProperty());
        custNumColumn.setCellValueFactory(cellData -> cellData.getValue().custNumProperty());
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().itemProperty());
        paymentMethodColumn.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }

    private void fetchDataFromDatabase() {
        try {
            String query = "SELECT invoice_no, custnum, item, payment_method, date, stock, price FROM ms_bl";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String invoiceNo = resultSet.getString("invoice_no");
                String custNum = resultSet.getString("custnum");
                String item = resultSet.getString("item");
                String paymentMethod = resultSet.getString("payment_method");
                String date = resultSet.getString("date");
                int stock = resultSet.getInt("stock");
                double price = resultSet.getDouble("price");

                billEntries.add(new BillEntry(invoiceNo, custNum, item, paymentMethod, date, stock, price));
            }

            Billinghistory.setItems(billEntries);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching data from the database: " + e.getMessage());
            System.out.println(e);
        }
    }

    private void setupSearchFilter() {
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                Billinghistory.setItems(billEntries);
                return;
            }

            ObservableList<BillEntry> filteredList = FXCollections.observableArrayList();

            for (BillEntry entry : billEntries) {
                if (entry.getCustNum().contains(newValue)) {
                    filteredList.add(entry);
                }
            }

            Billinghistory.setItems(filteredList);
        });
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
    public void add_product(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Inventory.fxml"));
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
    public void billing(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/Billing.fxml"));
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
    public void cu_list(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../FXML/CUSTOMERLIST.fxml"));
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

}
