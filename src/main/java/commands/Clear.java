package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

public class Clear implements Command {
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        //RestAction<Void> voidRestAction = event.getTextChannel().deleteMessagesByIds();
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return null;
    }
}
