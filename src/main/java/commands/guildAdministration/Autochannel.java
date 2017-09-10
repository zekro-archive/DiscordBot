package commands.guildAdministration;

import com.sun.security.auth.callback.TextCallbackHandler;
import commands.Command;
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
            save();
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
            save();

            tc.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    String.format("Successfully unset auto channel state of `%s`.", vc.getName())
            ).build()).queue();
        }
    }

    public static void unsetChan(VoiceChannel vc) {
        autochans.remove(vc);
        save();
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

    private static void save() {

        File path = new File("SERVER_SETTINGS/");
        if (!path.exists())
            path.mkdir();

        HashMap<String, String> out = new HashMap<>();

        System.out.println(autochans.size());
        autochans.forEach((v, g) -> out.put(v.getId(), g.getId()));

        try {
            FileOutputStream fos = new FileOutputStream("SERVER_SETTINGS/autochannels.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(out);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void load(JDA jda) {
        File file = new File("SERVER_SETTINGS/autochannels.dat");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                HashMap<String, String> out = (HashMap<String, String>) ois.readObject();
                ois.close();

                out.forEach((vid, gid) -> {
                    Guild g = getGuild(gid, jda);
                    autochans.put(getVchan(vid, g), g);
                });

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
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
