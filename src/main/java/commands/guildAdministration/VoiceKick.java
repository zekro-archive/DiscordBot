package commands.guildAdministration;


import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class VoiceKick implements Command {

    public static HashMap<Member, VoiceChannel> kicks = new HashMap<>();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(1, event)) return;

        if (args[0].toLowerCase().equals("channel")) {
            if (args.length > 1) {

                StringBuilder sb = new StringBuilder();
                Arrays.stream(args).skip(1).forEach(s -> sb.append(" " + s));
                String vc = sb.toString().substring(1);
                if (event.getGuild().getVoiceChannelsByName(vc, true).size() > 0) {
                    SSSS.setVKICKCHANNEL(vc, event.getGuild());
                    event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set voice kick channel to `" + vc + "`").build()).queue();
                    return;
                } else {
                    event.getTextChannel().sendMessage(MSGS.error().setDescription("Please enter a valid voice channel existing on this guild.").build()).queue();
                    return;
                }

            } else {
                event.getTextChannel().sendMessage(MSGS.error().setDescription("Please enter a valid voice channel existing on this guild.").build()).queue();
                return;
            }
        }

        if (event.getMessage().getMentionedUsers().size() > 0) {

            int timeout;
            try {
                StringBuilder sb = new StringBuilder();
                Arrays.stream(args).forEach(s -> sb.append(" " + s));
                timeout = Integer.parseInt(sb.toString().substring(1).replace("@" + event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getEffectiveName(), "").replaceAll(" ", ""));
            } catch (Exception e) {
                timeout = 0;
            }

            List<VoiceChannel> vc = event.getGuild().getVoiceChannelsByName(SSSS.getVKICKCHANNEL(event.getGuild()), true);

            if (vc.size() == 0) {
                event.getTextChannel().sendMessage(MSGS.error().setDescription("There is no voice channel set or it is no more existent on this guild.\nPlease set a valid voice channel with `-vkick channel <channel name>`.").build()).queue();
                return;
            }

            Member victim = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
            VoiceChannel current = event.getMember().getVoiceState().getChannel();
            event.getGuild().getController().moveVoiceMember(victim, vc.get(0)).queue();

            if (timeout > 0) {

                kicks.put(victim, current);

                event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(new Color(0xFF0036))
                    .setDescription(":boot:  " + event.getAuthor().getAsMention() + " (*" + event.getMember().getRoles().get(0).getName() + "*) kicked " + victim.getAsMention() +
                            " out of the voice channel `" + current.getName() + "` with a rejoin timeout of **" + timeout + " seconds**.")
                    .build()
                ).queue();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        kicks.remove(event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)));
                        victim.getUser().openPrivateChannel().complete().sendMessage("Your voice kick timeout has expired. You can now rejoin the channel `" + current.getName() + "`.").queue();
                    }
                }, timeout * 1000);
            } else {
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(new Color(0xFF0036))
                        .setDescription(":boot:  " + event.getAuthor().getAsMention() + " (*" + event.getMember().getRoles().get(0).getName() + "*) kicked " + victim.getAsMention() +
                                " out of the voice channel `" + current.getName() + "`.")
                        .build()
                ).queue();
            }

        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: \n" +
                "**-vkick <@user> (<timeout in seconds>)**  -  `Kick someone out of the voice channel (if timeout is entered, the kicked user cant rejoin the channel in timeout period)`\n" +
                "**-vkick channel <channel name>**  -  `Set the voice kick channel`";
    }

    @Override
    public String description() {
        return "Kick people out of your voice channel";
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
