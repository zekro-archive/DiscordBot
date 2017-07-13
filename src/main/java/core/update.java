package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.STATICS;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by zekro on 22.03.2017 / 15:59
 * DiscordBot / core
 * © zekro 2017
 */


public class update {

    private static String lastUpdate = "";

    public static String versionURL = "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/LATESTVERSION.txt";

    private static HashMap<String, Map.Entry<String, String>> getVersionInfo() throws IOException {

        String API_URL = "https://api.github.com/repos/zekrotja/DiscordBot/releases";

        HashMap<String, Map.Entry<String, String>> out = new HashMap<>();

        URL url = new URL(API_URL);
        Scanner s = new Scanner(url.openStream());
        String output = "";
        while (s.hasNextLine()) {
            output += s.nextLine();
        }

        try {

            JSONArray jsonarray = new JSONArray(output);

            List<JSONObject> jsonobs = new ArrayList<>();
            for (int i = 0; i < jsonarray.length(); i++) {
                jsonobs.add(jsonarray.getJSONObject(i));
            }


            JSONObject pre = jsonobs.stream().filter(j -> {
                try {
                    return j.getString("prerelease").equals("true");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }).findFirst().orElse(null);

            JSONObject stable = jsonobs.stream().filter(j -> {
                try {
                    return j.getString("prerelease").equals("false");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }).findFirst().orElse(null);

            out.put("pre", new AbstractMap.SimpleEntry<>(pre.getString("tag_name"), pre.getString("html_url")));
            out.put("stable", new AbstractMap.SimpleEntry<>(stable.getString("tag_name"), stable.getString("html_url")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static void manualCheck(TextChannel channel) {

        try {

            if (!getVersionInfo().get("pre").getKey().equals(STATICS.VERSION)) {

                if (!STATICS.BOT_OWNER_ID.isEmpty()) {

                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(new Color(0x7EFF00))
                                    .setDescription(
                                            "**New bot update is available!**\n" +
                                                    "Download the latest version and install it manually on your vServer.\n\n" +
                                                    "You are currently running on version: **" + STATICS.VERSION + "**\n\n")
                                    .addField("Latest Prerelease Build", "Version: " + getVersionInfo().get("pre").getKey() + "\nDownload: " + getVersionInfo().get("pre").getValue(), false)
                                    .addField("Latest Stable Build", "Version: " + getVersionInfo().get("stable").getKey() + "\nDownload: " + getVersionInfo().get("stable").getValue(), false)
                                    .setFooter("Enter '-disable' to disable this message on new updates.", null)
                                    .build()
                    ).queue();

                }
            } else {

                channel.sendMessage(new EmbedBuilder().setColor(Color.green)
                    .setDescription("The bot is currently up to date!")
                    .build()
                ).queue();

            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public static boolean checkIfUpdate(JDA jda) {

        if (new File("SERVER_SETTINGS/no_update_info").exists())
            return false;

        try {

            if (!getVersionInfo().get("pre").getKey().equals(STATICS.VERSION) && !lastUpdate.equals(getVersionInfo().get("pre").getKey())) {
                lastUpdate = getVersionInfo().get("pre").getKey();

                if (!STATICS.BOT_OWNER_ID.isEmpty()) {

                    jda.getUserById(STATICS.BOT_OWNER_ID).openPrivateChannel().complete().sendMessage(
                            new EmbedBuilder()
                                    .setColor(new Color(0x7EFF00))
                                    .setDescription(
                                            "**New bot update is available!**\n" +
                                            "Download the latest version and install it manually on your vServer.\n\n" +
                                            "You are currently running on version: **" + STATICS.VERSION + "**\n\n")
                                    .addField("Latest Prerelease Build", "Version: " + getVersionInfo().get("pre").getKey() + "\nDownload: " + getVersionInfo().get("pre").getValue(), false)
                                    .addField("Latest Stable Build", "Version: " + getVersionInfo().get("stable").getKey() + "\nDownload: " + getVersionInfo().get("stable").getValue(), false)
                                    .setFooter("Enter '-disable' to disable this message on new updates.", null)
                                    .build()
                    ).queue();

                }

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        return false;
    }
}
