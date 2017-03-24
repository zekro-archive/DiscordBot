package commands.administration;

import commands.Command;
import core.coreCommands;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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

        if (!coreCommands.checkPermission(event)) {
            event.getTextChannel().sendMessage(
                    ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
            ).queue();
            return;
        }

        event.getTextChannel().sendMessage(":warning: :mobile_phone_off:   " + event.getAuthor().getAsMention() + " shut down " + event.getJDA().getSelfUser().getAsMention() + " because of maintenance or an unexpected behavior.").queue();
        System.exit(0);
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
}
