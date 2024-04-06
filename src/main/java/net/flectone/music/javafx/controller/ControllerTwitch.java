package net.flectone.music.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.flectone.music.Main;
import net.flectone.music.twitch.TwitchHandler;
import org.controlsfx.control.Notifications;


import java.net.URL;
import java.util.ResourceBundle;

public class ControllerTwitch implements Initializable {

    @FXML
    PasswordField identityProviderTextField;
    @FXML
    PasswordField accessTokenTextField;
    @FXML
    TextField channelIDTextField;
    @FXML
    TextField channelNameTextField;
    @FXML
    TextField getRewardTextField;
    @FXML
    TextField addRewardTextField;
    @FXML
    TextField skipRewardTextField;
    @FXML
    Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TwitchHandler twitchHandler = Main.twitchHandler;
        if (twitchHandler == null) return;

        identityProviderTextField.setText(twitchHandler.getIdentityProvider());
        accessTokenTextField.setText(twitchHandler.getAccessToken());
        channelIDTextField.setText(twitchHandler.getChannelID());
        channelNameTextField.setText(twitchHandler.getChannelName());

        getRewardTextField.setText(twitchHandler.getGetReward());
        addRewardTextField.setText(twitchHandler.getAddReward());
        skipRewardTextField.setText(twitchHandler.getSkipReward());
    }

    public void saveButtonAction() {
        String identityProvider = identityProviderTextField.getText();
        String accessToken = accessTokenTextField.getText();
        String channelID = channelIDTextField.getText();
        String channelName = channelNameTextField.getText();
        String getReward = getRewardTextField.getText();
        String addReward = addRewardTextField.getText();
        String skipReward = skipRewardTextField.getText();

        if (identityProvider.isEmpty() || accessToken.isEmpty() || channelID.isEmpty() || channelName.isEmpty()) {
            Notifications.create()
                    .title("Ошибка настройки")
                    .text("Обязательные поля должны быть заполнены")
                    .position(Pos.CENTER)
                    .showError();
            return;
        }

        if (getReward.isEmpty()) getReward = null;
        if (addReward.isEmpty()) addReward = null;
        if (skipReward.isEmpty()) skipReward = null;

        Main.setTwitchHandler(new TwitchHandler(identityProvider, accessToken, channelID, channelName,
                getReward, addReward, skipReward));
    }
}
