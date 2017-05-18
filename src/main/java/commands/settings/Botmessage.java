package commands.settings;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by zekro on 17.05.2017 / 13:55
 * DiscordBot/commands.administration
 * © zekro 2017
 */

public class Botmessage implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(2, event)) return;

        StringBuilder message = new StringBuilder();
        Arrays.stream(args).forEach(s -> message.append(s + " "));
        String messageString = message.substring(0, message.length() - 1);
        event.getJDA().getPresence().setGame(Game.of(messageString + " | -help | v." + STATICS.VERSION));
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription("Successfully set bot message to `" + messageString + "`!").build()).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -botmsg <message>";
    }

    @Override
    public String description() {
        return "Set the \"Playing ...\" message of the bot.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.settings;
    }
}
