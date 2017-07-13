package listeners;

import commands.chat.Vote2;
import commands.etc.Bug;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.MSGS;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zekro on 24.05.2017 / 12:55
 * DiscordBot/listeners
 * © zekro 2017
 */
public class reactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent event) {

        System.out.println(event.getMessageId().equals(Bug.MESSAGE.getId()) + " - " + event.getUser().equals(Bug.AUTHOR));
        if (event.getMessageId().equals(Bug.MESSAGE.getId()) && event.getUser().equals(Bug.AUTHOR)) {
            System.out.println("TEST");
            Bug.CHANNEL.sendMessage(Bug.FINAL_MESSAGE.build()).queue();
            Bug.sendConfMessage();

            Bug.AUTHOR = null;
            Bug.MESSAGE = null;
            Bug.FINAL_MESSAGE = null;
            Bug.CHANNEL = null;
            Bug.TIMER = null;
        }

    }

}
