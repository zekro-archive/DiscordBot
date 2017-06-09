package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        File f = new File("screenlog.0");

        if (f.exists()) {

            BufferedReader br = new BufferedReader(new FileReader(f));
            List<String> logLines = new ArrayList<>();
            List<String> outLogLines;
            StringBuilder sb = new StringBuilder();
            String shorted = "Unshorted log.";

            br.lines().forEach(l -> logLines.add(l));

            outLogLines = logLines;
            if (logLines.size() > 20) {
                shorted = "Log shorted because it is bigger than 20 lines.";
                outLogLines = outLogLines.subList(outLogLines.size() - 20, outLogLines.size());
            }

            outLogLines.forEach(s -> sb.append(s + "\n"));

            event.getTextChannel().sendMessage(
                    "__**zekroBot `screenlog.0` log**__\n" +
                         "*" + shorted + "*\n\n" +
                         "```" +
                         sb.toString() +
                         "```"
            ).queue();

        } else {

            event.getTextChannel().sendMessage(MSGS.error.setDescription("There is no file `'screenlog.0'` available to get log from.").build()).queue();

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
        return null;
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }
}
