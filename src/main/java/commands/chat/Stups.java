package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * © zekro 2017
 *
 * @author zekro
 */
public class Stups implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        String message = "";



        if (args.length > 0) {

            if (args[0].startsWith("@")) {

                if (!event.getGuild().isMember(event.getMessage().getMentionedUsers().get(0))) {
                    event.getTextChannel().sendMessage(
                            ":warning:   This user is not a member of this guild!"
                    ).queue();
                }

                if (args.length > 1) {
                    message += "\n\n:love_letter:  Nachicht: \"";
                    for (String s : Arrays.asList(args).subList(1, args.length)) {
                        message += s + " ";
                    }
                    message += "\"";
                }

                PrivateChannel pc = event.getMessage().getMentionedUsers().get(0).openPrivateChannel().complete();
                pc.sendMessage(
                        ":point_right:  **Stups!** (von " + event.getAuthor().getAsMention() + ")" + message
                ).queue();

                event.getMessage().delete().queue();
            }
        } else
            event.getTextChannel().sendMessage(
                    ":warning:   Please mention a user from this guild you want to send a nudge!"
            ).queue();



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -stups <@user> Message";
    }

    @Override
    public String description() {
        return "Give someone a nudge";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
    }
}
