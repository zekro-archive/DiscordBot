package commands.administration;

import commands.Command;
import core.Perms;
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

        if (Perms.test(event)) return;

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
