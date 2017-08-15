package commands.settings;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zekro on 17.05.2017 / 14:10
 * DiscordBot/commands.SettingsCore
 * © zekro 2017
 */

public class Prefix implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(permission(), event)) return;

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(":warning: Please enter a valid prefix!").build()).queue();
            return;
        }

        SSSS.setPREFIX(args[0], event.getGuild());
        event.getTextChannel().sendMessage(MSGS.success().setDescription("Prefix successfully changed to `" + args[0] + "`.").build()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -prefix <new prefix>";
    }

    @Override
    public String description() {
        return "Set the command prefix for this server";
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
