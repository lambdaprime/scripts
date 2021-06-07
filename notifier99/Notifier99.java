/**
 * Shows particular message every 20 minutes.
 *
 * Usage:
 *
 * - Linux
 *
 * jeval Notifier99.java &
 *
 * - Windows
 *
 * jeval Notifier99.java
 *
 * Requirements:
 *
 * - jeval <https://github.com/lambdaprime/jeval>
 *
 * @author lambdaprime <https://github.com/lambdaprime>
 */
//dependency org.openjfx:javafx-controls:11
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.beans.property.*;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.*;

import javafx.scene.media.*;  

import javafx.util.Duration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

final String TITLE = "Notifier99";
final int SCREEN_TIME_MINS = 20;
final int PAUSE_TIME_MINS = 5;

void showExpirationAlert(int seconds, Runnable onFinish) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setHeaderText("");
    alert.setContentText("Unexpected error, application will be closed");
    alert.setTitle(TITLE);

    var timeSeconds = new SimpleLongProperty(seconds);
    Label timerLabel = new Label();
    timerLabel.textProperty().bind(timeSeconds.asString());
    timerLabel.setTextFill(Color.RED);
    timerLabel.setStyle("-fx-font-size: 4em;");

    Instant end = Instant.now().plus(seconds, ChronoUnit.SECONDS); 
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), (event) -> {
        var now = Instant.now();
        var sec = java.time.Duration.between(now, end).toSeconds();
        if (end.isBefore(now) || sec <= 0) {
            timeSeconds.setValue(0);
            timeline.stop();
            onFinish.run();
            return;
        }
        timeSeconds.setValue(sec - 1);
    }));
    timeline.playFromStart();

    VBox vb = new VBox(20);
    vb.setAlignment(Pos.CENTER);
    vb.getChildren().addAll(timerLabel);
    vb.setLayoutY(30);

    alert.getDialogPane().setContent(vb);

    alert.setOnCloseRequest(event -> {
        onFinish.run();
    });

    alert.showAndWait();
}

class Notifier99 extends Application {

    @Override
    public void start(Stage stage) {
        while (true) {
            var player = playSound();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(TITLE);
            alert.setHeaderText("");
            alert.setContentText("Unexpected error, application will be closed");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true);
            alert.showAndWait();
            showExpirationAlert((int)TimeUnit.MINUTES.toSeconds(PAUSE_TIME_MINS), () -> {
                player.stop();
            });
            sleep(TimeUnit.MINUTES.toMillis(SCREEN_TIME_MINS));
        }
    }

    public static MediaPlayer playSound() {
        String path = scriptPath.get().resolveSibling("untie.wav").toString();
          
        //Instantiating Media class  
        Media media = new Media(new File(path).toURI().toString());  
          
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
         
        // play the audio
        mediaPlayer.setAutoPlay(true);

        return mediaPlayer;
    }

    public static void main(String[] args) {
        launch();
    }
}

Notifier99.main(null);
