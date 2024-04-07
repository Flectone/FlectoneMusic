package net.flectone.music.javafx.fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import net.flectone.music.Main;
import net.flectone.music.file.Config;
import net.flectone.music.javafx.controller.ControllerApp;
import net.flectone.music.util.FileUtils;

import java.io.IOException;

public class FlectoneMusic extends Application {

    @Getter
    private static ControllerApp controllerApp;

    private Stage twitchStage;

    private static Image icon;

    @Override
    public void start(Stage stage) throws IOException {
        icon = new Image("net/flectone/music/image/flectone.png");

        showAppScene(stage);
        stage.setOnCloseRequest(e -> stop());
    }

    public void stop() {
        FileUtils.saveToFile(controllerApp.getMediaQueue(), Config.MEDIA_QUEUE);
        FileUtils.saveToFile(Main.twitchHandler, Config.TWITCH);

        System.exit(1);
    }

    private void showAppScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("app.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setTitle("FlectoneMusic - музыка стримера и его зрителей");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();

        controllerApp = fxmlLoader.getController();
    }

    public void showTwitchScene() throws IOException {
        if (twitchStage == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("twitch.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            twitchStage = new Stage();
            twitchStage.setResizable(false);
            twitchStage.setTitle("Настройка Twitch");
            twitchStage.setScene(scene);
            twitchStage.setAlwaysOnTop(true);
            twitchStage.getIcons().add(icon);
        }

        if (twitchStage.isShowing()) return;

        twitchStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
