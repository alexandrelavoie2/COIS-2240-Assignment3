import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RentalSystemGUI extends Application {
    private Scene scene;
    private final VBox vbMainMenu = new VBox(14);
    private final VBox vbAddVehicleMenu = new VBox(14);
    private final VBox vbAddCarMenu = new VBox(14);
    private final TextField tfCarPlate = new TextField();
    private final TextField tfCarMake = new TextField();
    private final TextField tfCarModel = new TextField();
    private final TextField tfCarYear = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Button btnAddVehicle = createMenuButton("1: Add Vehicle");
        Button btnAddCustomer = createMenuButton("2: Add Customer");
        Button btnRentVehicle = createMenuButton("3: Rent Vehicle");
        Button btnReturnVehicle = createMenuButton("4: Return Vehicle");
        Button btnDisplayAvailable = createMenuButton("5: Display Available Vehicles");
        Button btnShowHistory = createMenuButton("6: Show Rental History");
        Button btnExit = createMenuButton("0: Exit");

        btnAddVehicle.setOnAction(event -> showAddVehicleMenu());
        btnExit.setOnAction(event -> stage.close());

        configureMenuBox(vbMainMenu);
        vbMainMenu.getChildren().addAll(
                btnAddVehicle,
                btnAddCustomer,
                btnRentVehicle,
                btnReturnVehicle,
                btnDisplayAvailable,
                btnShowHistory,
                btnExit);

        buildAddVehicleMenu();
        buildAddCarMenu();

        scene = new Scene(vbMainMenu, 700, 650);
        stage.setTitle("Vehicle Rental System GUI");
        stage.setScene(scene);
        stage.show();
    }

    private void buildAddVehicleMenu() {
        Label lblTitle = new Label("Add Vehicle");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnCar = createMenuButton("1: Car");
        Button btnMinibus = createMenuButton("2: Minibus");
        Button btnPickupTruck = createMenuButton("3: Pickup Truck");
        Button btnBack = createMenuButton("0: Back");

        btnCar.setOnAction(event -> showAddCarMenu());
        btnBack.setOnAction(event -> showMainMenu());

        configureMenuBox(vbAddVehicleMenu);
        vbAddVehicleMenu.getChildren().addAll(lblTitle, btnCar, btnMinibus, btnPickupTruck, btnBack);
    }

    private void buildAddCarMenu() {
        Label lblTitle = new Label("Add Car");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label lblPlate = new Label("Enter license plate");
        tfCarPlate.setPromptText("e.g. AAA111");
        Label lblMake = new Label("Enter make");
        tfCarMake.setPromptText("e.g. Toyota");
        Label lblModel = new Label("Enter model");
        tfCarModel.setPromptText("e.g. Corolla");
        Label lblYear = new Label("Enter year");
        tfCarYear.setPromptText("e.g. 2019");

        Button btnAddCar = createMenuButton("Add Car");
        Button btnBack = createMenuButton("0: Back");

        btnBack.setOnAction(event -> showAddVehicleMenu());

        configureMenuBox(vbAddCarMenu);
        vbAddCarMenu.getChildren().addAll(
                lblTitle,
                lblPlate,
                tfCarPlate,
                lblMake,
                tfCarMake,
                lblModel,
                tfCarModel,
                lblYear,
                tfCarYear,
                btnAddCar,
                btnBack);
    }

    private void configureMenuBox(VBox vbMenu) {
        vbMenu.setSpacing(14);
        vbMenu.setPadding(new Insets(24));
        vbMenu.setAlignment(Pos.CENTER);
    }

    private void showMainMenu() {
        scene.setRoot(vbMainMenu);
    }

    private void showAddVehicleMenu() {
        scene.setRoot(vbAddVehicleMenu);
    }

    private void showAddCarMenu() {
        scene.setRoot(vbAddCarMenu);
    }

    private Button createMenuButton(String sText) {
        Button btnMenu = new Button(sText);
        btnMenu.setMaxWidth(Double.MAX_VALUE);
        btnMenu.setMaxHeight(Double.MAX_VALUE);
        btnMenu.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        VBox.setVgrow(btnMenu, Priority.ALWAYS);
        return btnMenu;
    }
}
