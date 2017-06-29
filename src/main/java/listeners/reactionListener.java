package listeners;

import commands.music.Music;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

/**
 * Created by zekro on 24.05.2017 / 12:55
 * DiscordBot/listeners
 * © zekro 2017
 */
public class reactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent event) {

        if (event.getMessageId().equals(Music.PLAYERMESSAGE.getId())) {

            List<MessageReaction> reactions = Music.PLAYERMESSAGE.getReactions();
            System.out.println(reactions.size());

            if (event.getReaction().equals(reactions.get(0))) {
                Music.getPlayer(Music.guild).setPaused(!Music.getPlayer(Music.guild).isPaused());
            }

        }

    }

}
