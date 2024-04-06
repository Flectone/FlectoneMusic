package net.flectone.music.flectonemusicapp.javafx.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import net.flectone.music.flectonemusicapp.javafx.FlectoneMusicApp;

public class KeyListener implements NativeKeyListener {

    public KeyListener() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {

        switch (e.getKeyCode()) {
            case NativeKeyEvent.VC_MEDIA_NEXT -> Platform.runLater(() -> FlectoneMusicApp.getController().mediaSkipButtonAction());
            case NativeKeyEvent.VC_MEDIA_PLAY -> Platform.runLater(() -> FlectoneMusicApp.getController().clickPlayer());
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }
    }

}

