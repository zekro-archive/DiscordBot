package commands;

import javafx.scene.control.Alert;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class Help implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        HashMap<String,String> cmdList = new HashMap<>();
        cmdList.put("alerts", Alerts.HELP);
        cmdList.put("bjoke", BJoke.HELP);
        cmdList.put("bj", BJoke.HELP);
        cmdList.put("cat", Cat.HELP);
        cmdList.put("clear", Clear.HELP);
        cmdList.put("8ball", EightBall.HELP);
        cmdList.put("ping", "PONG!");

        if (args.length > 0) {
            if (cmdList.containsKey(args[0]))
                event.getTextChannel().sendMessage(cmdList.get(args[0])).queue();
            else
                event.getTextChannel().sendMessage(":warning:  The command list does not contains information for the command ` ~" + args[0] + " ` !").queue();
            return;
        }

        event.getTextChannel().sendMessage(
                        "**__GENERAL COMMANDS:__** \n\n" +
                        "` ~ping `  -  Pong!\n" +
                        "` ~cat `  -  =^..^=\n" +
                        "` ~8ball `  -  Ask the holy 8ball for a decision!\n" +
                        "` ~clear `  -  *No function yet*\n" +
                        "` ~bjoke ` alt: ` ~bj `  -  Special fun tool for special guys :^)\n" +
                        "` ~c `  -  Cancels ~bjoke command\n" +
                        "` ~alerts `  -  Paste list of current warframe alerts\n" +
                        "` ~clear `  -  Clear an specific amount of messages in chat\n" +
                        "` ~ttt `  -  Get server name and password of our TTT server" +
                        "` ~say `  -  Say something with the bot's chat voice" +
                        "` ~info `  -  Info\n" +
                        "` ~help <command> `  -  Get more information about command"
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
