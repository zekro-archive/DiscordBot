package commands.chatutil;

import commands.Command;
import commands.chatutil.BJoke;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / commands
 * © zekro 2017
 */



public class BJokeCancle implements Command {

    public static boolean canceled = false;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (BJoke.called) {
            if (BJoke.victim.getUser().getName() == event.getAuthor().getName()) {
                event.getTextChannel().sendMessage(
                        "Du kannst dich nicht selbst aus deinem Schicksal befreien, " + BJoke.victim.getUser().getName() + "!"
                ).queue();
            } else {
                canceled = true;
            }
        } else {
            canceled = true;
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
