package net.flectone.music.flectonemusicapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

    private static final Pattern ID_PATTERN = Pattern.compile("^.*(youtu.be/|v/|u/\\w/|embed/|watch\\?v=|&v=)([^#&?]*).*");
    private static final Pattern YOUTUBE_PATTERN = Pattern.compile("((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?");

    public static String getId(String url) {
        Matcher matcher = ID_PATTERN.matcher(url);
        if (matcher.find() && matcher.group(2).length() == 11) {
            return matcher.group(2);
        } else {
            return null;
        }
    }


    public static String getYouTubeUrl(String string) {
        System.out.println(string);
        Matcher matcher = YOUTUBE_PATTERN.matcher(string);

        return matcher.find() ? matcher.group().replace(",", "") : null;
    }
}
