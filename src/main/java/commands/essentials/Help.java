package commands.essentials;

import commands.Command;
import core.Main;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.*;

/**
 * Created by zekro on 24.03.2017 / 19:46
 * DiscordBot / commands
 * © zekro 2017
 */


public class Help implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (args.length > 0) {
            if (Main.commands.containsKey(args[0]))
                if (Main.commands.get(args[0]).help() != null)
                    event.getTextChannel().sendMessage(Main.commands.get(args[0]).help()).queue();
                else
                    event.getTextChannel().sendMessage(":warning:  There are currently no information for the command ` -" + args[0] + " ` !").queue();
            else
                event.getTextChannel().sendMessage(":warning:  The command list does not contains information for the command ` -" + args[0] + " ` !").queue();
            return;
        }

        event.getMessage().delete().queue();

        Map<String, String> cmds = new TreeMap<>();
        Main.commands.forEach((s, command) -> cmds.put(s, command.description()));
        StringBuilder commandsInvokesAsMessageString = new StringBuilder();

        String[] ignorers = {"c", "m", "bj", "ttt", "userinfo"};
        Arrays.stream(ignorers).forEach(s -> cmds.remove(s));

        cmds.forEach((s, s2) -> commandsInvokesAsMessageString.append(
                ":white_small_square:  **" + s + "**   -   `" + s2 + "`\n"
        ));

        //ArrayList<String> cmdInvokes = new ArrayList<>();
        //ArrayList<String> cmdHelp = new ArrayList<>();
        //Main.commands.forEach((s, command) -> cmdInvokes.add(s));
        //Main.commands.forEach((s, command) -> cmdHelp.add(command.description() != null ? " *-   " + command.description() + "*" : ""));
        //String commandsInvokesAsMessageString = "";
        //int index = 0;
        //for ( String s : cmdInvokes ) {
        //    commandsInvokesAsMessageString += "**" + STATICS.PREFIX + s + "**  " + cmdHelp.get(index) + "\n";
        //    index++;
        //}

        try {

            PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
            pc.sendMessage(
                    ":clipboard:  __**COMMAD LIST**__  :clipboard: \n\n" +
                            "If you want a full list of commands with description, please take a look there:\n" +
                            ":point_right:   **http://zekrosbot.zekro.de**\n\n" +
                            commandsInvokesAsMessageString

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
