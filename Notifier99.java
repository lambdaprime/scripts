/**
 * Shows particular message every 20 minutes.
 *
 * Usage: CLASSPATH=<JAVAFX_SDK>/* jeval Notifier99.java
 *
 * Requirements:
 *
 * - JavaFX SDK
 * - jeval
 *
 * @author lambdaprime <https://github.com/lambdaprime>
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

class Notifier99 extends Application {

    @Override
    public void start(Stage stage) {
        while (true) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notifier99");
            alert.setHeaderText("");
            alert.setContentText("Unexpected error, application will be closed");
            alert.showAndWait();
            sleep(TimeUnit.MINUTES.toMillis(20));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

Notifier99.main(null);