package commands.guildAdministration;

import commands.Command;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

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

            final VoiceChannel vc = event.getGuild().getVoiceChannelsByName(sb.toString().substring(0, sb.toString().length() - 1), true).get(0);

            if (event.getMember().getVoiceState().inVoiceChannel()) {
                if (!event.getMember().getVoiceState().getChannel().equals(vc)) {
                    event.getMember().getVoiceState().getChannel().getMembers()
                            .forEach(member -> event.getGuild().getController().moveVoiceMember(member, vc).queue());
                } else {
                    event.getTextChannel().sendMessage(
                            MSGS.error.setDescription("You don't need to move everyone in the same channel you are still in...").build()
                    ).queue();
                }
            } else
                event.getTextChannel().sendMessage(
                        MSGS.error.setDescription("You need to be in a voice channel to use this command!").build()
                ).queue();

        } catch (IndexOutOfBoundsException e) {
            event.getTextChannel().sendMessage(
                    MSGS.error.setDescription("Please enter a valid voice channel existing on this guild!").build()
            ).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -moveall <voicechannel>";
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
