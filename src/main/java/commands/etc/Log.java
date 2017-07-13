package commands.etc;

import com.google.gson.JsonParser;
import commands.Command;
import core.Perms;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;
import utils.MSGS;
import utils.STATICS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zekro on 10.06.2017 / 00:39
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class Log implements Command {


    /**
     * Method by StupPlayer (https://github.com/StupPlayer)
     * @param data
     * @return
     */
    public static String hastePost(String data) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://hastebin.com/documents");

        try {
            post.setEntity(new StringEntity(data));

            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            return "https://hastebin.com/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Could not post!";
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        File f = new File("screenlog.0");

        if ((args.length > 0 ? args[0] : "").equalsIgnoreCase("clear")) {

            if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

            if (f.exists()) {
                if (f.delete()) {
                    f.createNewFile();
                    event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully cleared log file.").build()).queue();
                } else {
                    event.getTextChannel().sendMessage(MSGS.error().setDescription("Failed while attempting to clear log file.").build()).queue();
                }
            }
            return;
        }

        if (f.exists()) {

            BufferedReader br = new BufferedReader(new FileReader(f));
            List<String> logLines = new ArrayList<>();
            List<String> outLogLines;
            StringBuilder sb = new StringBuilder();
            //StringBuilder sbFull = new StringBuilder();
            //String shorted = "Unshorted log.";
            //Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Uploading log to hastebin.com ...").build()).complete();

            br.lines().forEach(l -> logLines.add(l));
            //logLines.forEach(l -> sbFull.append(l + "\n"));

            outLogLines = logLines;
            if (logLines.size() > 20) {
                //shorted =   "Log shorted because it is longer than 20 lines.\n" +
                //            "See full log here: " + hastePost(sbFull.toString());
                outLogLines = outLogLines.subList(outLogLines.size() - 20, outLogLines.size());
            }

            outLogLines.forEach(s -> sb.append(s + "\n"));

            //msg.delete().queue();

            event.getTextChannel().sendMessage(
                    "__**zekroBot `screenlog.0` log**__\n\n" +
                         //"*" + shorted + "*\n\n" +
                         "```" +
                         sb.toString() +
                         "```"
            ).queue();

        } else {

            event.getTextChannel().sendMessage(MSGS.error().setDescription("There is no file `'screenlog.0'` available to get log from.").build()).queue();

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Show bots log file";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }

    @Override
    public int permission() {
        return 0;
    }
}
