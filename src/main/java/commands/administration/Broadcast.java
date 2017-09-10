package commands.administration;

import commands.Command;
import core.Perms;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zekro on 11.06.2017 / 10:10
 * DiscordBot/commands.administration
 * © zekro 2017
 */

public class Broadcast implements Command {


    private List<Guild> getIgnores(JDA jda) {
        File f = new File("broadcast_ignores.txt");
        List<Guild> out = new ArrayList();

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {}
            return out;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            br.lines().forEach(l -> {
                Guild g = jda.getGuildById(l.replace("\n", ""));
                if (g != null)
                    out.add(g);
            });
        } catch (Exception e) {}

        return out;
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        if (args.length < 1) {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(help()).build()).queue();
            return;
        }

        String message = String.join(" ", args);
        User author = event.getAuthor();

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.cyan)
                .setAuthor(author.getName(), null, author.getAvatarUrl())
                .setDescription(message)
                .setFooter("This is a broadcast message send by bots host user.", "https://raw.githubusercontent.com/zekroTJA/DiscordBot/dev/.websrc/zekrobot_message.png");

        List<Guild> ignores = getIgnores(event.getJDA());
        event.getJDA().getGuilds().stream()
                .filter(g -> !ignores.contains(g))
                .forEach(g -> {
                    try {
                        g.getPublicChannel().sendMessage(eb.build()).queue();
                    } catch (Exception e) { }
                });

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:**\n" +
               "`-boradcast <Message>`\n\n" +
               "**IMPORTANT:** If you want to exclude a several servers, create a text file in the bots location named `broadcast_ignores.txt` and enter there in every line the ID of the ignored servers.";
    }

    @Override
    public String description() {
        return "Send a message to all servers general chats";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }

    @Override
    public int permission() {
        return 3;
    }
}
