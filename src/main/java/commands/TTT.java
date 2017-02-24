package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TTT implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(
                ":regional_indicator_t: :regional_indicator_t: :regional_indicator_t:\n\n" +
                "Servername:    ` GrandHarzer `\n" +
                "Passwort:    ` LOLIGLIG `\n"
        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
