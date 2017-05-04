package commands.chat;

import commands.Command;
import core.Perms;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import core.coreCommands;
import utils.MSGS;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / commands
 * © zekro 2017
 */



public class Clear implements Command {

    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public static String HELP = ":warning:  USAGE: ` ~clear <amount ob messages (>2)> to clear an amount of chat messages`";


    private int getInt(String arg) {

        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            return 0;
        }

    }

    public void action(String[] args, MessageReceivedEvent event) {

        if (Perms.test(event)) return;

        try {
            MessageHistory history = new MessageHistory(event.getTextChannel());
            List<Message> msgs;

            if (args.length < 1 || (args.length > 0 ? getInt(args[0]) : 1) == 1 && (args.length > 0 ? getInt(args[0]) : 1) < 2) {

                event.getMessage().delete().queue();
                msgs = history.retrievePast(2).complete();
                msgs.get(0).delete().queue();

                Message answer = event.getTextChannel().sendMessage(MSGS.success.setDescription(
                        "Successfully deleted last message!"
                ).build()).complete();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        answer.delete().queue();
                    }
                }, 3000);

            } else if (getInt(args[0]) <= 100) {

                event.getMessage().delete().queue();
                msgs = history.retrievePast(getInt(args[0])).complete();
                event.getTextChannel().deleteMessages(msgs).queue();

                Message answer = event.getTextChannel().sendMessage(MSGS.success.setDescription(
                        "Successfully deleted " + args[0] + " messages!"
                ).build()).complete();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        answer.delete().queue();
                    }
                }, 3000);
            } else {
                event.getTextChannel().sendMessage(MSGS.error
                        .addField("Error Type", "Message value out of bounds.", false)
                        .addField("Description", "The entered number if messages can not be more than 100 messages!", false)
                        .build()
                ).queue();
            }
        } catch (Exception e) {
            event.getTextChannel().sendMessage(MSGS.error
                    .addField("Error Type", e.getLocalizedMessage(), false)
                    .addField("Message", e.getMessage(), false)
                    .build()
            ).queue();
        }

    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return HELP;
    }

    @Override
    public String description() {
        return "Bulk delete chat messages";
    }
}
