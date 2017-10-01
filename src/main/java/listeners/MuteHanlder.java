package listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.Color;
import java.util.HashMap;

/**
 * Created by zekro on 14.09.2017 / 22:06
 * DiscordBot.listeners
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */
public class MuteHanlder extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        User sender = e.getAuthor();
        HashMap<String, String> mutes = commands.guildAdministration.Mute.getMuted();

        if (mutes.containsKey(sender.getId())) {
            sender.openPrivateChannel().queue(pc -> pc.sendMessage(
                    new EmbedBuilder().setColor(Color.orange).setDescription(
                            "You can not write on this server, because you are muted in text channels!\n" +
                            "Please contact an Supporter, Moderator or Admin to unmute.\n\n" +
                            "Mute Reason: `" + mutes.get(sender.getId()) + "`"
                    ).build()
            ).queue());
            e.getMessage().delete().queue();
        }
    }

}
