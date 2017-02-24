package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.ParseException;

public interface Command {

    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event) throws ParseException;
    void executed(boolean success, MessageReceivedEvent event);
    String help();
}

