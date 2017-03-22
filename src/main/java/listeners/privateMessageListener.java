package listeners;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * © zekro 2017
 *
 * @author zekro
 */
public class privateMessageListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        PrivateChannel pc = null;
        try {
            pc = event.getAuthor().openPrivateChannel().complete();
            pc.sendMessage(
                    ":warning:  Hey! Please don't send private messages to the bot's account! Sorry, but nobody will receive them. :crying_cat_face: "
            ).queue();
        } catch (Exception e) {}


    }

}
