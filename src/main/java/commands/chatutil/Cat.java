package commands.chatutil;

import commands.Command;
import commands.chatutil.BJokeCancle;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / commands
 * © zekro 2017
 */



public class Cat implements Command {
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public static String HELP = ":warning:  USAGE: ` ~cat ` to post a single cat picture or use ` ~cat spam <time preiod in seconds> ` to post periodically cat pictures (stop with ` ~c `) =^..^=";

    int counter = 0;
    public void action(String[] args, MessageReceivedEvent event) {

        try {

            Timer timer = new Timer();
            if (args[0].equals("spam") && Integer.parseInt(args[1]) >= 10) {
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        counter++;

                        if (BJokeCancle.canceled) {
                            return;
                        }
                        event.getTextChannel().sendMessage("CAT NUMBER: " + counter).queue();
                        throwCat(event);

                    }},0, Integer.parseInt(args[1])*1000);
            } else if (Integer.parseInt(args[1]) < 10) {
                event.getTextChannel().sendMessage("Please enter a period over 9 seconds to avoid overspam.").queue();
                return;
            } else if (!args[0].equals("spam")) {
                event.getTextChannel().sendMessage(help()).queue();
            }

        } catch (Exception e) {
            throwCat(event);
        }
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return HELP;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void throwCat(MessageReceivedEvent event) {

        JSONObject json = null;
        try {
            json = readJsonFromUrl("http://random.cat/meow");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String outputMessage = json.get("file").toString();

        event.getTextChannel().sendMessage(outputMessage).queue();
    }
}
