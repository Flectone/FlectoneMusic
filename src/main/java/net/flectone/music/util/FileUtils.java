package net.flectone.music.util;

import net.flectone.music.Main;
import net.flectone.music.file.Config;
import net.flectone.music.file.Script;
import net.flectone.music.twitch.TwitchHandler;

import java.io.*;
import java.util.Map;
import java.util.Queue;

public class FileUtils {

    public static void load(Map<Script, String> map) {
        try {

            for (Script script : Script.values()) {

                InputStream inputStream = Main.class.getResourceAsStream(Config.SCRIPT.getName() + "/" + script.getName());
                if (inputStream == null) return;

                String scriptContent = new String(inputStream.readAllBytes());
                map.put(script, scriptContent);

                System.out.println(script + " " + scriptContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(Queue<String> queue) {
        if (!new File(Config.MEDIA_QUEUE.getName()).exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(Config.MEDIA_QUEUE.getName()))) {
            reader.lines().forEach(queue::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TwitchHandler load(Config config) {
        if (!new File(config.getName()).exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(config.getName()))) {
            String identityProvider = reader.readLine();
            String accessToken = reader.readLine();
            String channelID = reader.readLine();
            String channelName = reader.readLine();

            if (identityProvider == null || accessToken == null || channelID == null || channelName == null) return null;

            String getReward = reader.readLine();
            String addReward = reader.readLine();
            String skipReward = reader.readLine();

            return new TwitchHandler(identityProvider, accessToken, channelID, channelName, getReward, addReward, skipReward);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void saveToFile(Queue<String> queue, Config config) {
        if (queue == null) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(config.getName()))) {

            if (queue.isEmpty()) {
                writer.write("");
            } else {
                queue.forEach(writer::println);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(TwitchHandler twitchHandler, Config config) {
        if (twitchHandler == null) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getName()))) {
            writer.write(twitchHandler.getIdentityProvider() + "\n");
            writer.write(twitchHandler.getAccessToken() + "\n");
            writer.write(twitchHandler.getChannelID()+ "\n");
            writer.write(twitchHandler.getChannelName() + "\n");
            writer.write(twitchHandler.getGetReward() + "\n");
            writer.write(twitchHandler.getAddReward() + "\n");
            writer.write(twitchHandler.getSkipReward() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
