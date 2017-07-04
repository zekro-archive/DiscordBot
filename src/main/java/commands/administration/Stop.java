package commands.administration;

import commands.Command;
import core.Perms;
import core.coreCommands;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zekro on 23.03.2017 / 21:13
 * DiscordBot / commands
 * © zekro 2017
 */


public class Stop implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        event.getTextChannel().sendMessage(":warning: :mobile_phone_off:   " + event.getAuthor().getAsMention() + " shut down " + event.getJDA().getSelfUser().getAsMention() + " because of maintenance or an unexpected behavior.").queue();
        System.exit(0);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -stop";
    }

    @Override
    public String description() {
        return "Emergency stop the bot.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.administration;
    }

    @Override
    public int permission() {
        return 3;
    }
}
