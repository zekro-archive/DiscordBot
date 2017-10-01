package commands.guildAdministration;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * Created by zekro on 14.05.2017 / 10:34
 * DiscordBot/commands.etc
 * © zekro 2017
 *
 * THIS COMMAND IS CREATED BY CONTRIBUTOR naix (https://github.com/NaixTimo)
 */


public class Mute implements Command {


    private static HashMap<String, String> mutes = new HashMap<>();

    private static final File SAVE = new File("SERVER_SETTINGS/mutes.dat");

    private void save() {


        if (!SAVE.exists()) {
            try {
                SAVE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        } else {
            try {
                BufferedWriter br = new BufferedWriter(new FileWriter(SAVE));
                mutes.forEach((id, rs) -> {
                    try {
                        br.write(String.format("%s:::%s\n", id, rs));
                    } catch (IOException e) { e.printStackTrace(); }
                });
                br.close();
            } catch (IOException e) { e.printStackTrace(); }
        }

    }

    public static void load() {

        if (!SAVE.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVE));
            br.lines().forEach(l -> {
                String[] split = l.replace("\n", "").split(":::");
                mutes.put(split[0], split[1]);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String, String> getMuted() {
        return mutes;
    }


	private void toggle(String[] args, Message msg, TextChannel tc) {

	    User victim = msg.getMentionedUsers().size() > 0 ? msg.getMentionedUsers().get(0) : msg.getJDA().getUserById(args[0]);

	    if (victim == null) {
	        tc.sendMessage(MSGS.error().setDescription("Please enter a valid mention or user ID!").build()).queue();
	        return;
        }

        String vicid = victim.getId();
        if (mutes.containsKey(vicid)) {
	        mutes.remove(vicid);
	        save();
	        tc.sendMessage(MSGS.success().setDescription(String.format("%s unmuted %s.", msg.getAuthor().getAsMention(), victim.getAsMention())).build()).queue();
        } else {
            String reason = "No reason.";
            if (args.length > 1)
                reason = String.join(" ", Arrays.asList(args).subList(1, args.length));
            mutes.put(vicid, reason);
            save();
            tc.sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(String.format("%s muted %s.\n\nReason: `%s`", msg.getAuthor().getAsMention(), victim.getAsMention(), reason)).build()).queue();
        }

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) { return false; }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        Guild guild = event.getGuild();
        Member author = event.getMember();
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();

        if (args.length < 1) {
            tc.sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        toggle(args, msg, tc);

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
         return "USAGE:\n" +
                "**mute <@mention / userID> <reason>**  -  `Mute/Unmute a Member`";
    }

    @Override
    public String description() {
        return "Mute/Unmute a Member";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.guildadmin;
    }

    @Override
    public int permission() {
        return 2;
    }
}
