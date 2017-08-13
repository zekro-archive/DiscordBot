package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

public class TTT implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (!event.getGuild().getId().equals("297298167634853890")) return;

        event.getTextChannel().sendMessage(
                ":regional_indicator_t: :regional_indicator_t: :regional_indicator_t:\n\n" +
                "Servername:    ` GrandHarzer `\n" +
                "Passwort:    ` LOLIGLIG `\n\n" +
                "**Essentials**\n\n" +
                "CSS Content Pack: \n*http://www.mediafire.com/file/hz3oqbgxl67lrbi/CSS_Content_Addon-Jan2015.zip*\n\n" +
                "Addons: \n*http://steamcommunity.com/sharedfiles/filedetails/?id=868387326*\n\n" +
                "TTT Weapon Pack: \n*http://www.mediafire.com/file/utaqgvy2q6rv4if/ttt_weapons_pack_v1-1.0.0.zip*\n\n" +
                "**Installation der Content Packages:**\n\n" +
                "In Steam: Rechtsklick auf \"Garry's Mod\" > \"Eigenschaften\" > \"Lokale Dateien\" > \"Lokale Dateien durchsuchen...\"\n" +
                "Nun die **Ordner** aus den Archiven in den Pfad ~/garrysmod/addons/ schieben (**nicht** die Dateien aus den Ordnern in den Pfad!)"
        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "<BETA COMMAND>";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
    }

    @Override
    public int permission() {
        return 0;
    }
}
