package commands.chatutil;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.permsCore;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / commands
 * © zekro 2017
 */


public class Say implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (!permsCore.checkPermission(event)) {
            event.getTextChannel().sendMessage(
                    ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
            ).queue();
            return;
        }

        String serverID = event.getGuild().getId();
        String channel = event.getTextChannel().getName();

        event.getMessage().deleteMessage().queue();

        String output = "";
        if (args.length > 0) {
            for (String e : args) {
                output += e + " ";
            }
        }

        event.getTextChannel().sendMessage(":loudspeaker:   " + output).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
