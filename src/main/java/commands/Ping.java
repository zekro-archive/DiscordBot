package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping implements Command {

    private final String HELP = "USAGE: ~ping";

    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("Pong!").queue();
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {

        return HELP;
    }

    @Override
    public String description() {
        return "Pong!";
    }
}
