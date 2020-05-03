/**
 * Shows particular message every 20 minutes.
 *
 * Usage:
 *
 * - Linux
 *
 * export CLASSPATH=/media/x/javafx-sdk-11.0.2/lib/*
 * cd $(dirname $(readlink -f "$0"))
 * jeval Notifier99.java &
 *
 * - Windows
 *
 * set CLASSPATH=<JAVAFX_SDK>/lib/*
 * jeval %~dp0/Notifier99.java
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

import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;

import javafx.util.Duration;

class Notifier99 extends Application {

    @Override
    public void start(Stage stage) {
        while (true) {
            var player = playSound();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notifier99");
            alert.setHeaderText("");
            alert.setContentText("Unexpected error, application will be closed");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true);
            alert.showAndWait();
            player.stop();
            sleep(TimeUnit.MINUTES.toMillis(20));
        }
    }

    public static MediaPlayer playSound() {
        String path = "untie.wav";
          
        //Instantiating Media class  
        Media media = new Media(new File(path).toURI().toString());  
          
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
         
        //by setting this property to true, the audio will be played   
        mediaPlayer.setAutoPlay(true);

        return mediaPlayer;
    }

    public static void main(String[] args) {
        launch();
    }
}

Notifier99.main(null);
//Notifier99.playSound();
