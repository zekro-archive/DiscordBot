package commands.guildAdministration;

import commands.Command;
import core.coreCommands;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Ringo Hoffmann on 26.03.2017.
 */
public class Kick implements Command {
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

        String reason = "none";
        if (args.length > 1) {
            reason = args[1];
        }

        event.getTextChannel().sendMessage(
                ":small_red_triangle_down:  " + event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention() + " got kicked by " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ").\n\n" +
                        "Reason: " + reason
        ).queue();

        PrivateChannel pc = event.getMessage().getMentionedUsers().get(0).openPrivateChannel().complete();
        pc.sendMessage(
                "Sorry, you got kicked from Server " + event.getGuild().getName() + " by " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ").\n\n" +
                        "Reason: " + reason
        ).queue();


        event.getGuild().getController().kick(
                event.getGuild().getMember(
                        event.getMessage().getMentionedUsers().get(0)
                )
        ).queue();
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
