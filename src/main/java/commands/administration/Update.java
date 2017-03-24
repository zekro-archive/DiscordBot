package commands.administration;

import commands.Command;
import core.coreCommands;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;
import core.update;

/**
 * Created by zekro on 22.03.2017 / 17:13
 * DiscordBot / commands
 * © zekro 2017
 */


public class Update implements Command {
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

        if (!core.update.checkIfUpdate()) {
            event.getTextChannel().sendMessage(
                    ":warning:   The bot is up to date! ;)"
            ).queue();
            return;
        }

        core.update.getUpdate(event.getJDA().getGuilds());

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
        return "Update discord bot.";
    }
}
