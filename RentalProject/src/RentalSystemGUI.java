import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RentalSystemGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label lblMessage = new Label("Vehicle Rental System GUI");
        StackPane spRoot = new StackPane(lblMessage);
        Scene scene = new Scene(spRoot, 500, 300);

        stage.setTitle("Vehicle Rental System GUI");
        stage.setScene(scene);
        stage.show();
    }
}
