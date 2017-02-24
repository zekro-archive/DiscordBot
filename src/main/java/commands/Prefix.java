package commands;

import core.coreCommands;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.xmlParser;
import org.xml.sax.SAXException;
import utils.STATICS;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Prefix implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (!coreCommands.checkPermission(event)) {
            event.getTextChannel().sendMessage(
                    ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
            ).queue();
            return;
        }

        try {
            System.out.println(args[0]);
            xmlParser.editValue("servers/" + event.getGuild().getName() + "/settings.xml", args[0]);
            STATICS.PREFIX = args[0];
            System.out.println(STATICS.PREFIX);
            event.getTextChannel().sendMessage("Prefix successdully changed to " + args[0]).queue();
        } catch (TransformerException e) {
            event.getTextChannel().sendMessage("[ERROR] Prefix change failed! (TransformerException)").queue();
            e.printStackTrace();
        } catch (SAXException e) {
            event.getTextChannel().sendMessage("[ERROR] Prefix change failed! (SAXException)").queue();
            e.printStackTrace();
        } catch (IOException e) {
            event.getTextChannel().sendMessage("[ERROR] Prefix change failed! (IOException)").queue();
            e.printStackTrace();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
