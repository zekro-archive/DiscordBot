package commands.settings;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by zekro on 17.05.2017 / 15:12
 * DiscordBot/commands.SettingsCore
 * © zekro 2017
 */
public class ServerLeftMessage implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(permission(), event)) return;

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        Arrays.stream(args).forEach(s -> sb.append(s + " "));

        SSSS.setSERVERLEAVEMESSAGE(sb.toString().substring(0, sb.toString().length() - 1), event.getGuild());
        event.getTextChannel().sendMessage(MSGS.success().setDescription("Server leave message successfully changed to `" + sb.toString().substring(0, sb.toString().length() - 1) + "`.").build()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -leavemsg <message>\n" +
                "Set message to `OFF` to disable server leave message.\n" +
                "Use `[USER]` to mention leaved user and `[GUILD]` to enter guild name in message.";
    }

    @Override
    public String description() {
        return "Set the message when a guild member leaves the guild.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.settings;
    }

    @Override
    public int permission() {
        return 3;
    }
}
