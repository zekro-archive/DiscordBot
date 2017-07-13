package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MSGS;
import utils.STATICS;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class Cat implements Command {
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    Timer spamTimer = new Timer();

    public void action(String[] args, MessageReceivedEvent event) {

        switch (args.length > 0 ? args[0].toLowerCase() : "") {

            case "spam":

                int delay;
                try {
                    delay = Integer.parseInt(args[1]);
                    if (delay < 5) {
                        delay = 5;
                        event.getTextChannel().sendMessage(MSGS.error.setDescription("The minumum time period is 5 seconds because of spam protection. ;)").build()).queue();
                    }
                } catch (Exception e) {
                    delay = 10;
                }

                List<String> counter = new ArrayList<>();
                spamTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        counter.add("");
                        event.getTextChannel().sendMessage("Cat number: `[" + (counter.size() + 1) + "]`\n" + getCat()).queue();
                        if (counter.size() > 999) {
                            spamTimer.cancel();
                            spamTimer = new Timer();
                        }
                    }
                }, 0, delay * 1000);

                break;

            case "s":
            case "stop":

                spamTimer.cancel();
                spamTimer = new Timer();

                break;

            default:
                event.getTextChannel().sendMessage(getCat()).queue();
                event.getMessage().delete().queue();
                break;

        }

    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return "USAGE:\n" +
                "**cat**  -  `Returns a cute cat picture`\n" +
                "**cat spam <period in secs>**  -  `Spams cat pictures automatically`\n" +
                "**cat stop**  -  `Stops the cat spam`";
    }

    @Override
    public String description() {
        return "=^..^=";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
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

    public static String getCat() {

        JSONObject json = null;
        try {
            json = readJsonFromUrl("http://random.cat/meow");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String outputMessage = null;
        try {
            outputMessage = json.get("file").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return outputMessage;
    }


    @Override
    public int permission() {
        return 0;
    }
}
