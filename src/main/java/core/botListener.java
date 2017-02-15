package core;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

public class botListener extends ListenerAdapter{

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContent().startsWith(STATICS.PREFIX) && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId()) {
            Main.handleCommand(Main.parser.parse(e.getMessage().getContent()/*.toLowerCase()*/, e));
        }
    }

    @Override
    public void onReady(ReadyEvent e) {
        //core.Main.log("STATUS ", "LOGGED IN AS: " + e.getJDA().getSelfUser().getName());
    }

}
