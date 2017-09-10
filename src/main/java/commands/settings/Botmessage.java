package commands.settings;

import commands.Command;
import core.Perms;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by zekro on 17.05.2017 / 13:55
 * DiscordBot/commands.administration
 * © zekro 2017
 */

public class Botmessage implements Command {


    private static boolean custom = false;
    private static int members = 0;

    private static void count() {
        members++;
    }

    public static void setSupplyingMessage(JDA jda) {

        if (!custom) {
            jda.getGuilds().forEach(g -> g.getMembers().forEach(m -> count()));
            jda.getPresence().setGame(Game.of("Supplying " + members + " users" + " | -help | v." + STATICS.VERSION));
            members = 0;
        }
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        custom = true;

        String messageString = String.join(" ", args);

        if (messageString.equals("off")) {
            custom = false;
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription("Successfully set botmsg to standard setting!").build()).queue();
            return;
        }

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

    @Override
    public int permission() {
        return 3;
    }
}
