package listeners;

import commands.chat.Vote2;
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

        //if (Vote2.voteHash.keySet().stream().anyMatch(guild -> Vote2.voteHash.get(guild).message.getId().equals(event.getMessageId()))) {
        //    event.getReaction().removeReaction().queue();
        //    event.getChannel().sendMessage(MSGS.error.setDescription("Please don't add reactions to this poll message!").build()).queue(message -> {
        //        new Timer().schedule(new TimerTask() {
        //            @Override
        //            public void run() {
        //                message.delete().queue();
        //            }
        //        }, 3000);
        //    });
        //}

    }

}
