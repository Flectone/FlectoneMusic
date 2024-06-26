package net.flectone.music.javafx.controller;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.Getter;
import net.flectone.music.Main;
import net.flectone.music.file.Script;
import net.flectone.music.util.CookieUtils;
import net.flectone.music.util.FileUtils;
import net.flectone.music.util.TimeUtils;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Getter
public class ControllerApp implements Initializable {

    // UI elements
    @FXML
    WebView mediaWebView;
    @FXML
    WebView playerWebView;
    @FXML
    Button playerButton;
    @FXML
    Button skipButton;
    @FXML
    Button settingButton;
    @FXML
    Button mediaTurnButton;
    @FXML
    TextField playerTextField;
    @FXML
    ListView<String> mediaQueueList;

    private boolean isEnableMedia = true;

    // WebEngines for WebView
    private WebEngine playerWebEngine;
    private WebEngine mediaWebEngine;

    // MediaBridge instance
    private MediaBridge mediaBridge;

    // Queue for media URLs
    private final Queue<String> mediaQueue = new LinkedList<>();

    private static final Map<Script, String> SCRIPTS = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileUtils.load(SCRIPTS);

        CookieUtils.loadCookies();
        setupPlayer();
        setupMedia();
    }

    private void setupPlayer() {
        playerTextField.setText("https://music.yandex.ru/home");
        playerWebEngine = playerWebView.getEngine();
        playerWebEngine.setUserAgent("Mozilla/5.0");

        playerWebEngine.setJavaScriptEnabled(true);

        playerWebEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                playerWebEngine.executeScript(SCRIPTS.get(Script.AUTO_SKIP_SONG));
                CookieUtils.saveCookies();
            }
        });
        loadPage();
    }

    private void setupMedia() {
        mediaWebEngine = mediaWebView.getEngine();
        mediaWebEngine.setUserAgent("Mozilla/5.0");
        mediaWebEngine.setJavaScriptEnabled(true);

        mediaWebEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                try {
                    injectScript(mediaWebEngine);
                } catch (Exception ignored) {}
            }
        });

        mediaBridge = new MediaBridge(this, mediaWebEngine, mediaQueue);

        FileUtils.load(mediaQueue);

        if (mediaQueue.isEmpty()) {
            mediaWebEngine.load(null);
        } else {
            mediaBridge.playNext(false);
        }
    }

    // Inject JavaScript into the web page
    private void injectScript(WebEngine webEngine) {
        System.out.println("Successful script injection");

        // Setting up MediaBridge for communication between Java and JavaScript
        JSObject jsobj = (JSObject) webEngine.executeScript("window");
        mediaBridge = new MediaBridge(this, mediaWebEngine, mediaQueue);
        jsobj.setMember("media", mediaBridge);

        // check video time
        String time = String.valueOf(webEngine.executeScript(SCRIPTS.get(Script.GET_MEDIA_TIME)));

        if (TimeUtils.get(time) > 250) {
            mediaBridge.playNext(true);
            return;
        }

        webEngine.executeScript(SCRIPTS.get(Script.AUTO_NEXT_MEDIA));
        webEngine.executeScript(SCRIPTS.get(Script.AUTO_SKIP_MEDIA));
    }

    // Inner class to bridge between Java and JavaScript
    public static class MediaBridge {
        private final ControllerApp controllerApp;
        private final WebEngine mediaWebEngine;
        private final Queue<String> mediaQueue;

        public MediaBridge(ControllerApp controllerApp, WebEngine mediaWebEngine, Queue<String> mediaQueue) {
            this.controllerApp = controllerApp;
            this.mediaWebEngine = mediaWebEngine;
            this.mediaQueue = mediaQueue;
        }

        // Play next media, optionally skipping the current one
        public void playNext(boolean skip) {
            Platform.runLater(() -> {
                if (controllerApp.isEnableMedia()) {

                    if (!skip && controllerApp.getMediaName() != null) {
                        controllerApp.pausePlayer();
                        return;
                    }

                    controllerApp.updateMediaQueueListView();
                    String media = mediaQueue.poll();

                    if (media != null) {
                        mediaWebEngine.load(media);
                        controllerApp.pausePlayer();
                        return;
                    }

                    mediaWebEngine.load(null);
                }

                if (skip) {
                    controllerApp.nextPlayer();
                } else {
                    controllerApp.playPlayer();
                }
            });
        }
    }

    public void loadPage() {
        playerWebEngine.load(playerTextField.getText());
    }

    public void playPlayer() {
        try {
            playerWebEngine.executeScript(SCRIPTS.get(Script.PLAY_SONG));
        } catch (JSException e) {
            e.printStackTrace();
        }
    }

    public void pausePlayer() {
        try {
            playerWebEngine.executeScript(SCRIPTS.get(Script.PAUSE_SONG));
        } catch (JSException e) {
            e.printStackTrace();
        }
    }

    public void nextPlayer() {
        try {
            playerWebEngine.executeScript(SCRIPTS.get(Script.NEXT_SONG));
        } catch (JSException e) {
            e.printStackTrace();
        }
    }

    // Add music to media queue
    public void addMusic(String url) {
        Platform.runLater(() -> {
            System.out.println(url);
            mediaQueue.add(url);
            updateMediaQueueListView();
            mediaBridge.playNext(false);
        });
    }

    // Skip media
    public void mediaSkipButtonAction() {
        mediaBridge.playNext(true);
    }

    // Click player
    public void clickPlayer() {
        playerWebEngine.executeScript(SCRIPTS.get(Script.CLICK_SONG));
    }

    private String getMediaName() {
        try {
            Object object = mediaWebEngine.executeScript(SCRIPTS.get(Script.GET_MEDIA_NAME));
            if (object != null) return String.valueOf(object).trim();
        } catch (JSException ignored) {}

        return null;
    }

    // Get current track name
    public String getCurrentTrackName() {
        String string = getMediaName();

        return string != null
                ? string
                : String.valueOf(playerWebEngine.executeScript(SCRIPTS.get(Script.GET_SONG_NAME))).trim();
    }

    private void updateMediaQueueListView() {
        Platform.runLater(() -> {
            mediaQueueList.getItems().clear();
            mediaQueueList.getItems().addAll(mediaQueue);
        });
    }

    public void showSettingTwitch() throws IOException {
        Main.app.showTwitchScene();
    }

    public void mediaTurnButtonAction() {
        if (isEnableMedia) {
            mediaTurnButton.setText("Включить реквесты");
            isEnableMedia = false;

        } else {
            mediaTurnButton.setText("Выключить реквесты");
            isEnableMedia = true;
        }

        mediaWebEngine.executeScript(SCRIPTS.get(Script.CLICK_MEDIA));
        mediaBridge.playNext(false);
    }


}
