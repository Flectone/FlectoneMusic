package net.flectone.music.file;

import lombok.Getter;

@Getter
public enum Config {

    TWITCH("twitch.txt"),
    MEDIA_QUEUE("media_queue.txt"),
    COOKIES("cookies.json");

    private final String name;

    Config(String name) {
        this.name = name;
    }
}
