package net.flectone.music.util;

public class TimeUtils {

    public static int get(String string) {
        String[] stringTime = string.split(":");

        int time = 0;
        for (int x = 0; x < stringTime.length; x++) {
            time += (int) (Integer.parseInt(stringTime[x]) * Math.pow(60, stringTime.length - x - 1));
        }

        return time;
    }

}
