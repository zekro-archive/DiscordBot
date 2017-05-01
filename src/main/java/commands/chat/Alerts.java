package commands.chat;

import commands.Command;
import core.Perms;
import core.coreCommands;
import core.warframeAlertsCore;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.text.ParseException;

import static listeners.readyListener.restartWarframeAlertsCore;

public class Alerts implements Command {

    public static String HELP = ":warning:  USAGE: ` ~alerts ` for posting full list of alerts / ` ~alerts filter ` for open GDocs-Doscument for filters / ` ~alerts restart ` for restarting the warframe alerts core (Mods & Admins only)";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException {

        switch (args.length > 0 ? args[0] : "") {

            case "filter":
                event.getTextChannel().sendMessage(
                        ":pencil: https://docs.google.com/document/d/" + STATICS.DOCID_warframeAlertsFilter + "/edit"
                ).queue();
                break;

            case "restart":
                if (Perms.test(event)) return;
                restartWarframeAlertsCore();
                event.getTextChannel().sendMessage("Restarting complete.").queue();
                break;

            case "":
                event.getTextChannel().sendMessage(warframeAlertsCore.getAlertsAsMessage(warframeAlertsCore.getAlerts())).queue();

        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public String description() {
        return "Show a list of current warframe alerts / edit warframe alerts filters";
    }
}
