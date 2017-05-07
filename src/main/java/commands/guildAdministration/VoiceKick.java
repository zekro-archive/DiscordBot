package commands.guildAdministration;


import commands.Command;
import core.Perms;
import core.coreCommands;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class VoiceKick implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(1, event)) return;

        GuildController gc = new GuildController(event.getGuild());

        VoiceChannel kickToChannel = event.getGuild().getVoiceChannels().get(0);
        if (event.getGuild().getVoiceChannelsByName(STATICS.KICK_VOICE_CHANNEL, true).size() > 0)
            kickToChannel = event.getGuild().getVoiceChannelsByName(STATICS.KICK_VOICE_CHANNEL, true).get(0);

        List<User> victims = event.getMessage().getMentionedUsers();

        System.out.println(STATICS.KICK_VOICE_CHANNEL);
        for ( User u : victims ) {
            gc.moveVoiceMember(event.getGuild().getMember(u), kickToChannel).queue();
        }

        String kickedUsers = victims.get(0).getAsMention();
        if (victims.size() > 1) {
            kickedUsers = "";
            for ( User u : victims ) {
                kickedUsers += u.getAsMention() + ", ";
            }
            kickedUsers = kickedUsers.substring(0, kickedUsers.length() - 2);
        }

        event.getTextChannel().sendMessage(
                ":boot:  " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ") kicked " + kickedUsers + " out of Voice Channel."
        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -vkick <@user1> <@user2> ...";
    }

    @Override
    public String description() {
        return "Kick (multiple) people out of your voice channel";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.guildadmin;
    }
}
