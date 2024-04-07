package net.flectone.music.file;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum Script {

    AUTO_NEXT_MEDIA("auto_next_media.js"),
    AUTO_SKIP_MEDIA("auto_skip_media.js"),
    AUTO_SKIP_SONG("auto_skip_song.js"),
    CLICK_SONG("click_song.js"),
    GET_MEDIA_NAME("get_media_name.js"),
    GET_MEDIA_TIME("get_media_time.js"),
    GET_SONG_NAME("get_song_name.js"),
    NEXT_SONG("next_song.js"),
    PAUSE_SONG("pause_song.js"),
    PLAY_SONG("play_song.js");

    private final String name;

    @Setter
    private String action;

    Script(String name) {
        this.name = name;
    }

    public static Script fromString(String string) {
        return Arrays.stream(Script.values())
                .filter(script -> string.equalsIgnoreCase(script.getName()))
                .findFirst().orElse(null);
    }

}
