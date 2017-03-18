package listeners;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / listener
 * © zekro 2017
 */


public class privateMessageListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        PrivateChannel pc = null;
        try {
            pc = event.getAuthor().openPrivateChannel().block();
        } catch (RateLimitedException e) {}
        pc.sendMessage(
                ":warning:  Hey! Please don't send private messages to the bot's account! Sorry, but nobody will receive them. :crying_cat_face: "
        ).queue();

    }

}
