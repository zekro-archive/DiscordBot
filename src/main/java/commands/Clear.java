package commands;

import core.coreCommands;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Clear implements Command {
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    static String HELP = ":warning:  USAGE: ` ~clear <amount ob messages (>2)> to clear an amount of chat messages`";

    public void action(String[] args, MessageReceivedEvent event) {

        if (!coreCommands.checkPermission(event)) {
            event.getTextChannel().sendMessage(
                    ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
            ).queue();
            return;
        }

        if (args.length == 0) {
            event.getTextChannel().sendMessage(help()).queue();
        } else if (Integer.parseInt(args[0]) < 2) {
            event.getTextChannel().sendMessage(help()).queue();
        }

        MessageHistory history = new MessageHistory(event.getTextChannel());
        List<Message> msgs;

        try {
            msgs = history.retrievePast(Integer.parseInt(args[0])).block();
            event.getTextChannel().deleteMessages(msgs).queue();

            msgs = history.retrievePast(2).block();
            event.getTextChannel().deleteMessageById(msgs.get(0).getId()).queue();

            event.getTextChannel().sendMessage(args[0]+ " Messages deleted!").queue();

        } catch (Exception e) {
            e.printStackTrace();
        }

        MessageHistory historyAfter = new MessageHistory(event.getTextChannel());

        try {
            Thread.sleep(5000);

            msgs = historyAfter.retrievePast(1).block();
            event.getTextChannel().deleteMessageById(msgs.get(0).getId()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return HELP;
    }
}
