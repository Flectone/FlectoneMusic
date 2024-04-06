package net.flectone.music.flectonemusicapp;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import net.flectone.music.flectonemusicapp.file.Config;
import net.flectone.music.flectonemusicapp.javafx.FlectoneMusicApp;
import net.flectone.music.flectonemusicapp.javafx.listener.KeyListener;
import net.flectone.music.flectonemusicapp.twitch.TwitchHandler;
import net.flectone.music.flectonemusicapp.util.FileUtils;

public class Main {

    public static TwitchHandler twitchHandler;
    public static FlectoneMusicApp app;

    public static void main(String[] args) {
        GlobalScreen.addNativeKeyListener(new KeyListener());

        twitchHandler = FileUtils.load(Config.TWITCH);
        app = new FlectoneMusicApp();

        FlectoneMusicApp.main(args);
    }

    public static void setTwitchHandler(TwitchHandler twitchHandler) {
        if (Main.twitchHandler != null) Main.twitchHandler.close();
        Main.twitchHandler = twitchHandler;
    }
}
