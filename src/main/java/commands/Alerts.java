package commands;

import core.coreCommands;
import listeners.warframeAlertsCore;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
                        ":pencil: https://docs.google.com/document/d/13O2lZ_UemLDkCV8425XHOPSZ3aVoeYmV5cF_vLQAyEY/edit"
                ).queue();
                break;

            case "restart":
                if (!coreCommands.checkPermission(event)) {
                    event.getTextChannel().sendMessage(
                            ":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!"
                    ).queue();
                    return;
                }
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
}
