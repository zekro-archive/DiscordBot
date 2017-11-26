package commands.guildAdministration;

import com.sun.security.auth.callback.TextCallbackHandler;
import commands.Command;
import core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.Color;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zekro on 08.09.2017 / 20:25
 * DiscordBot.commands.guildAdministration
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */


public class Autochannel implements Command, Serializable {


    private static HashMap<VoiceChannel, Guild> autochans = new HashMap<>();

    public static HashMap<VoiceChannel, Guild> getAutochans() {
        return autochans;
    }

    private void setChan(String id, Guild g, TextChannel tc) {
        VoiceChannel vc = getVchan(id, g);

        if (vc == null) {
            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    String.format("Voice channel with the ID `%s` does not exist.", id)
            ).build()).queue();
        } else if (autochans.containsKey(vc)) {
            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    "This channel is just set as auto channel."
            ).build()).queue();
        } else {
            autochans.put(vc, g);
            saveVchan(vc, g);
            tc.sendMessage(new EmbedBuilder().setColor(Color.green).setDescription(
                    String.format("Successfully set voice channel `%s` as auto channel.", vc.getName())
            ).build()).queue();
        }
    }

    private void unsetChan(String id, Guild g, TextChannel tc) {
        VoiceChannel vc = getVchan(id, g);

        if (vc == null) {
            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    String.format("Voice channel with the ID `%s` does not exist.", id)
            ).build()).queue();
        } else if (!autochans.containsKey(vc)) {
            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    String.format("Voice channel `%s` is not set as auto channel.", vc.getName())
            ).build()).queue();
        } else {
            autochans.remove(vc);
            unsetChan(vc);

            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    String.format("Successfully unset auto channel state of `%s`.", vc.getName())
            ).build()).queue();
        }
    }

    public static void unsetChan(VoiceChannel vc) {
        autochans.remove(vc);
        Main.getMySql().dropEntry("autochans", "chan", vc.getId());
    }

    private void listChans(Guild guild, TextChannel tc) {
        StringBuilder sb = new StringBuilder().append("**Auto Channel**\n\n");
        autochans.keySet().stream()
                .filter(c -> autochans.get(c).equals(guild))
                .forEach(c -> sb.append(String.format(":white_small_square:   `%s` *(%s)*\n", c.getName(), c.getId())));
        tc.sendMessage(new EmbedBuilder().setDescription(sb.toString()).build()).queue();
    }

    private static VoiceChannel getVchan(String id, Guild g) {
        return g.getVoiceChannelById(id);
    }

    private static Guild getGuild(String id, JDA jda) {
        return jda.getGuildById(id);
    }

    private void saveVchan(VoiceChannel vc, Guild g) {
        try {
            PreparedStatement ps = Main.getMySql().getConn().prepareStatement(String.format("INSERT INTO autochans (chan, guild) VALUES ('%s', '%s')", vc.getId(), g.getId()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void load(JDA jda) {

        try {
            PreparedStatement ps = Main.getMySql().getConn().prepareStatement("SELECT * FROM autochans");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Guild g = getGuild(rs.getString("guild"), jda);
                autochans.put(getVchan(rs.getString("chan"), g), g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        Guild g = event.getGuild();
        TextChannel tc = event.getTextChannel();

        if (args.length < 1) {
            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(help()).build()).queue();
            return;
        }

        switch (args[0]) {

            case "list":
            case "show":
                listChans(g, tc);
                break;

            case "add":
            case "set":
                if (args.length < 2)
                    tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(help()).build()).queue();
                else
                    setChan(args[1], g, tc);
                break;

            case "remove":
            case "delete":
            case "unset":
                if (args.length < 2)
                    tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(help()).build()).queue();
                else
                    unsetChan(args[1], g, tc);
                break;

            default:
                tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(help()).build()).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:**\n" +
               ":white_small_square:  `-autochannel set <Chan ID>`  -  Set voice chan as auto channel\n" +
               ":white_small_square:  `-autochannel unset <Chan ID>`  -  Unset voice chan as auto chan\n" +
               ":white_small_square:  `-autochannel list`  -  Display all registered auto chans\n";
    }

    @Override
    public String description() {
        return "Manage auto channel function";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.guildadmin;
    }

    @Override
    public int permission() {
        return 1;
    }
}
