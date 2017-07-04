package commands.administration;

import commands.Command;
import core.Perms;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        event.getTextChannel().sendMessage(":warning:  Bot will restart now...").queue();

        if (System.getProperty("os.name").toLowerCase().contains("linux"))
            Runtime.getRuntime().exec("screen sudo python restart.py");
        else
            Runtime.getRuntime().exec("wincmd.exe -restart");

        System.exit(0);

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -restart";
    }

    @Override
    public String description() {
        return "Restart the bot.";
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
