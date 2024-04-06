package net.flectone.music;


import com.github.kwhat.jnativehook.GlobalScreen;
import net.flectone.music.file.Config;
import net.flectone.music.javafx.fxml.FlectoneMusic;
import net.flectone.music.javafx.listener.KeyListener;
import net.flectone.music.twitch.TwitchHandler;
import net.flectone.music.util.FileUtils;

public class Main {

    public static TwitchHandler twitchHandler;
    public static FlectoneMusic app;

    public static void main(String[] args) {

        GlobalScreen.addNativeKeyListener(new KeyListener());

        twitchHandler = FileUtils.load(Config.TWITCH);
        app = new FlectoneMusic();

        FlectoneMusic.main(args);
    }

    public static void setTwitchHandler(TwitchHandler twitchHandler) {
        if (Main.twitchHandler != null) Main.twitchHandler.close();
        Main.twitchHandler = twitchHandler;
    }
}
