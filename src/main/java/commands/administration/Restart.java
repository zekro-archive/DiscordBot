package commands.administration;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zekro on 24.03.2017 / 19:49
 * DiscordBot / commands.administration
 * © zekro 2017
 */


public class Restart implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (
                event.getMember().getRoles().contains(event.getGuild().getRolesByName("Developer", false))
             || event.getMember().getRoles().contains(event.getGuild().getRolesByName("Admin", false))
             || event.getMember().getRoles().contains(event.getGuild().getRolesByName("Owner", false))
        ) {

            event.getTextChannel().sendMessage(":warning:  Bot will restart now...").queue();
            Runtime.getRuntime().exec("screen python restart.py");
            System.exit(0);

        } else {
            event.getTextChannel().sendMessage(
                    ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
            ).queue();
        }

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
