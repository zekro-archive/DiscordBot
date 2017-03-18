package commands.chatutil;

import commands.Command;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / commands
 * © zekro 2017
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

                try {
                    PrivateChannel pc = event.getMessage().getMentionedUsers().get(0).openPrivateChannel().block();
                    pc.sendMessage(
                            ":point_right:  **Stups!** (von " + event.getAuthor().getAsMention() + ")" + message
                    ).queue();
                } catch (RateLimitedException e) {
                    e.printStackTrace();
                }

                event.getMessage().deleteMessage().queue();
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
        return null;
    }
}
