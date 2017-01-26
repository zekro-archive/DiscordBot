package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BJokeCancle implements Command {

    public static boolean canceled = false;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        canceled = true;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
