package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zekro on 03.06.2017 / 15:04
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class CmdLog implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (args.length < 1) {

            StringBuilder sb = new StringBuilder();

            List<ArrayList<String>> loglist = STATICS.cmdLog;

            if (loglist.size() > 50)
                loglist = loglist.subList(loglist.size() - 50, loglist.size());

            loglist.stream()
                    .filter(strings -> strings.get(0).equals(event.getGuild().getId()))
                    .forEach(s -> sb.append(
                            "`" + s.get(1) + "`  **[" + s.get(2) + "]**  \"" + s.get(3) + "\"\n"
                    ));

            event.getTextChannel().sendMessage(
                    "**Last 50 Commands:**\n\n" + sb.toString()
            ).queue();

        } else if (args[0].toLowerCase().equals("all")) {

            StringBuilder sb = new StringBuilder();

            List<ArrayList<String>> loglist = STATICS.cmdLog;

            if (loglist.size() > 50)
                loglist = loglist.subList(loglist.size() - 50, loglist.size());

            loglist.stream()
                    .forEach(s -> sb.append(
                            "`" + s.get(1) + "`  **[" + s.get(2) + "]**  \"" + s.get(3) + "\"\n"
                    ));

            event.getTextChannel().sendMessage(
                    new EmbedBuilder().addField("Last 50 Commands (all guilds):", sb.toString(),false)
                            .build()
            ).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return  "USAGE:\n" +
                "**cmdlog**  -  `Show the last 50 commands executed on the guild`\n" +
                "**cmdlog all**  -  `Show the last 50 commands executed on every guild`";
    }

    @Override
    public String description() {
        return "Show the last 50 commands executed on the guild / on all guilds.";
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
