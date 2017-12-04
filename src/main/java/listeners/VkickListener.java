package listeners;

import commands.guildAdministration.VoiceKick;
import core.SSSS;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by zekro on 27.05.2017 / 13:31
 * DiscordBot/listeners
 * © zekro 2017
 */

public class VkickListener extends ListenerAdapter {


    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

        if (VoiceKick.kicks.containsKey(event.getMember()) && event.getChannelJoined().equals(VoiceKick.kicks.get(event.getMember()))) {
            event.getGuild().getController().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelsByName(SSSS.getVKICKCHANNEL(event.getGuild()), true).get(0)).queue();
            event.getMember().getUser().openPrivateChannel().complete().sendMessage(
                    "Sorry, but you are blocked for this channel because of a voice kick!"
            ).queue();
        }

    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

        if (VoiceKick.kicks.containsKey(event.getMember()) && event.getChannelJoined().equals(VoiceKick.kicks.get(event.getMember()))) {
            event.getGuild().getController().moveVoiceMember(event.getMember(), event.getChannelLeft()).queue();
            event.getMember().getUser().openPrivateChannel().complete().sendMessage(
                    "Sorry, but you are blocked for this channel because of a voice kick!"
            ).queue();
        }

    }


}
