package listeners;

import commands.chat.Vote3;
import commands.etc.Bug;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by zekro on 24.05.2017 / 12:55
 * DiscordBot/listeners
 * © zekro 2017
 */
public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        Vote3.handleReaction(event);

        try {
            if (event.getMessageId().equals(Bug.MESSAGE.getId()) && event.getUser().equals(Bug.AUTHOR)) {
                Bug.CHANNEL.sendMessage(Bug.FINAL_MESSAGE.build()).queue();
                Bug.sendConfMessage();

                Bug.AUTHOR = null;
                Bug.MESSAGE = null;
                Bug.FINAL_MESSAGE = null;
                Bug.CHANNEL = null;
                Bug.TIMER = null;
            }
        } catch (Exception e) {}
    }

}
