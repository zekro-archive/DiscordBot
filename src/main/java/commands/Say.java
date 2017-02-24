package commands;

import core.coreCommands;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Say implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (!coreCommands.checkPermission(event)) {
            event.getTextChannel().sendMessage(
                    ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
            ).queue();
            return;
        }

        String serverID = event.getGuild().getId();
        String channel = event.getTextChannel().getName();

        MessageHistory history = new MessageHistory(event.getJDA().getGuildById(serverID).getTextChannelsByName(channel, false).get(0));
        List<Message> msgs;

        try {
            msgs = history.retrievePast(1).block();
            event.getJDA().getGuildById(serverID).getTextChannelsByName(channel, false).get(0).deleteMessageById(msgs.get(0).getId()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
