package net.flectone.music.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import javafx.application.Platform;
import lombok.Getter;
import net.flectone.music.javafx.fxml.FlectoneMusic;
import net.flectone.music.util.UrlUtils;

@Getter
public class TwitchHandler {

    private OAuth2Credential oAuth2Credential;
    private TwitchClient twitchClient;

    private final String identityProvider;
    private final String accessToken;
    private final String channelID;
    private final String channelName;
    private final String getReward;
    private final String addReward;
    private final String skipReward;

    public TwitchHandler(String identityProvider, String accessToken, String channelID, String channelName) {
        this(identityProvider, accessToken, channelID, channelName, null, null, null);
    }

    public TwitchHandler(String identityProvider, String accessToken, String channelID, String channelName,
                         String getReward, String addReward, String skipReward) {

        this.identityProvider = identityProvider;
        this.accessToken = accessToken;
        this.channelID = channelID;
        this.channelName = channelName;
        this.getReward = getReward;
        this.addReward = addReward;
        this.skipReward = skipReward;

        init();
    }

    public void init() {

        Thread thread = new Thread(() -> {
            oAuth2Credential = new OAuth2Credential(identityProvider, accessToken);
            twitchClient = TwitchClientBuilder.builder()
                    .withEnableHelix(true)
                    .withEnableChat(true)
                    .withEnablePubSub(true)
                    .withChatAccount(oAuth2Credential)
                    .build();

            twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(oAuth2Credential, channelID);

            twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, this::handleRewardRedeemedEvent);
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void handleRewardRedeemedEvent(RewardRedeemedEvent rewardRedeemedEvent) {
        String rewardString = rewardRedeemedEvent.toString();

        if (rewardString.contains("title=" + addReward)) {
            addMusic(rewardString);
        } else if (rewardString.contains("title=" + getReward)) {
            getMusic(rewardString);
        } else if (rewardString.contains("title=" + skipReward)) {
            skipMusic();
        }
    }

    private void addMusic(String rewardString) {
        String youtubeUrl = UrlUtils.getYouTubeUrl(rewardString);
        if (youtubeUrl != null) {
            System.out.println(youtubeUrl);
            Platform.runLater(() -> FlectoneMusic.getControllerApp().addMusic(youtubeUrl));
        }
    }

    private void getMusic(String rewardString) {
        Platform.runLater(() -> {
            String message = "@" +
                    getLoginFromString(rewardString) +
                    ", сейчас играет «" +
                    FlectoneMusic.getControllerApp().getCurrentTrackName() +
                    "»";

            twitchClient.getChat().sendMessage(channelName, message);
        });
    }

    private void skipMusic() {
        Platform.runLater(FlectoneMusic.getControllerApp()::mediaSkipButtonAction);
    }

    private String getLoginFromString(String string) {
        return string.substring(string.lastIndexOf("login") + 6, string.indexOf(", displayName"));
    }

    public void close() {
        twitchClient.close();
    }
}
