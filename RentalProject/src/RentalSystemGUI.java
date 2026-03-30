import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RentalSystemGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox vbRoot = new VBox(14);
        vbRoot.setPadding(new Insets(24));
        vbRoot.setAlignment(Pos.CENTER);

        Button btnAddVehicle = createMenuButton("1: Add Vehicle");
        Button btnAddCustomer = createMenuButton("2: Add Customer");
        Button btnRentVehicle = createMenuButton("3: Rent Vehicle");
        Button btnReturnVehicle = createMenuButton("4: Return Vehicle");
        Button btnDisplayAvailable = createMenuButton("5: Display Available Vehicles");
        Button btnShowHistory = createMenuButton("6: Show Rental History");
        Button btnExit = createMenuButton("0: Exit");

        btnExit.setOnAction(event -> stage.close());

        vbRoot.getChildren().addAll(
                btnAddVehicle,
                btnAddCustomer,
                btnRentVehicle,
                btnReturnVehicle,
                btnDisplayAvailable,
                btnShowHistory,
                btnExit);

        Scene scene = new Scene(vbRoot, 700, 650);
        stage.setTitle("Vehicle Rental System GUI");
        stage.setScene(scene);
        stage.show();
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
