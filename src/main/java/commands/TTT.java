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
                "Passwort:    ` LOLIGLIG `\n\n" +
                "**Essentials**\n\n" +
                "CSS Content Pack: http://www.mediafire.com/file/hz3oqbgxl67lrbi/CSS_Content_Addon-Jan2015.zip\n" +
                "Addons: http://steamcommunity.com/sharedfiles/filedetails/?id=868387326"
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
