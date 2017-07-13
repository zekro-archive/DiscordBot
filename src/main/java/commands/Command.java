package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;

public interface Command {

    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException;
    void executed(boolean success, MessageReceivedEvent event);
    String help();
    String description();
    String commandType();
    int permission();
}

