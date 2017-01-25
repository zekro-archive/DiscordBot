package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class Cat implements Command {
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {

        JSONObject json = null;
        try {
            json = readJsonFromUrl("http://random.cat/meow");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String outputMessage = json.get("file").toString();

        event.getTextChannel().sendMessage(outputMessage).queue();
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return null;
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
}
