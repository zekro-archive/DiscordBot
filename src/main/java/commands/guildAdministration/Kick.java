package commands.guildAdministration;

import commands.Command;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

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

        if (core.Perms.check(2, event)) return;

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
        return "Kick a member from the server.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.guildadmin;
    }

    @Override
    public int permission() {
        return 2;
    }
}
