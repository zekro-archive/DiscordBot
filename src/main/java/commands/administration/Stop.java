package commands.administration;

import commands.Command;
import commands.etc.BotStats;
import core.Perms;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zekro on 23.03.2017 / 21:13
 * DiscordBot / commands
 * © zekro 2017
 */


public class Stop implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        BotStats.save();

        //event.getTextChannel().sendMessage(":warning: :mobile_phone_off:   " + event.getAuthor().getAsMention() + " shut down " + event.getJDA().getSelfUser().getAsMention() + " because of maintenance or an unexpected behavior.").queue();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 1000);

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -stop";
    }

    @Override
    public String description() {
        return "Emergency stop the bot.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.administration;
    }

    @Override
    public int permission() {
        return 3;
    }
}
