package commands;

import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import core.Main;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.SECRETS;
import utils.STATICS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        event.getMessage().delete().queue();

        ArrayList<String> cmdInvokes = new ArrayList<>();
        ArrayList<String> cmdHelp = new ArrayList<>();
        Main.commands.forEach((s, command) -> cmdInvokes.add(s));
        Main.commands.forEach((s, command) -> cmdHelp.add(command.description() != null ? " *-   " + command.description() + "*" : ""));
        String commandsInvokesAsMessageString = "";
        int index = 0;
        for ( String s : cmdInvokes ) {
            commandsInvokesAsMessageString += "**" + STATICS.PREFIX + s + "**  " + cmdHelp.get(index) + "\n";
            index++;
        }

        try {

            PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
            pc.sendMessage(
                    "__**COMMAD LIST**__\n\n" +
                    commandsInvokesAsMessageString + "\n\n" +
                    "If you want a full list of commands with description, please take a look there:\n" +
                    ":point_right:   http://zekrosbot.zekro.de"
            ).queue();


        } catch (Exception e) {}


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
        return "\"Ich brauch keine Hilfe...\" :D";
    }
}
