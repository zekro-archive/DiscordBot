package commands.guildAdministration;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by zekro on 07.05.2017 / 10:55
 * DiscordBot/commands.guildAdministration
 * © zekro 2017
 */

public class Moveall implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(1, event)) return;

        try {

            StringBuilder sb = new StringBuilder();
            Arrays.stream(args).forEach(s -> sb.append(s + " "));
            String vcName = sb.toString().substring(0, sb.toString().length() - 1);

            final VoiceChannel vc = event.getGuild().getVoiceChannels().stream()
                    .filter(voiceChannel -> voiceChannel.getName().toLowerCase().contains(vcName.toLowerCase()))
                    .findFirst().get();

            if (event.getMember().getVoiceState().inVoiceChannel()) {
                if (!event.getMember().getVoiceState().getChannel().equals(vc)) {

                    int membersInChannel = event.getMember().getVoiceState().getChannel().getMembers().size();
                    String VCfrom = event.getMember().getVoiceState().getChannel().getName();
                    String VCto = vc.getName();

                    event.getMember().getVoiceState().getChannel().getMembers()
                            .forEach(member -> event.getGuild().getController().moveVoiceMember(member, vc).queue());

                    event.getMessage().delete().queue();
                    event.getTextChannel().sendMessage(new EmbedBuilder().setColor(new Color(0, 169, 255))
                            .setDescription("Moved **" + membersInChannel + "** members from `" + VCfrom + "` to `" + VCto + "`.")
                            .build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));

                } else {
                    event.getTextChannel().sendMessage(
                            MSGS.error.setDescription("You don't need to move everyone in the same channel you are still in...").build()
                    ).queue();
                }
            } else
                event.getTextChannel().sendMessage(
                        MSGS.error.setDescription("You need to be in a voice channel to use this command!").build()
                ).queue();

        } catch (NoSuchElementException e) {
            event.getTextChannel().sendMessage(
                    MSGS.error.setDescription("Please enter a valid voice channel existing on this guild!").build()
            ).queue();
        } catch (Exception e) {
            event.getTextChannel().sendMessage(
                    MSGS.error
                        .addField("Error Message", e.getMessage(), false)
                    .build()
            ).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -moveall <voicechannel (can also just be a part of a voice channels name)>";
    }

    @Override
    public String description() {
        return "Move everyone in your channel to another channel.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.guildadmin;
    }
}
