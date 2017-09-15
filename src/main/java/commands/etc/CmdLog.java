package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
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

    private String saveCut(String input) {
        System.out.println(input);
        if (input.length() > 1900)
            return input.substring(0, 1900);
        return input;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (args.length < 1) {

            if (event.getGuild().getTextChannelsByName("cmdlog", true).size() < 1) {
                event.getGuild().getController().createTextChannel("cmdlog").queue();
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green).setDescription(
                        "Created channel #cmdlog.\nAll Commands, executed on this guild, will be logged in this channel.\nIf you want to disbale this feature, just delete this channel."
                ).build()).queue();
            } else {
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
                        "**Last 50 Commands:**\n\n" + saveCut(sb.toString())
                ).queue();
            }


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
                    "**Last 50 Commands (all guilds):** \n\n" + saveCut(sb.toString())
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
