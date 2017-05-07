package commands.essentials;

import commands.Command;
import core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.util.*;

/**
 * Created by zekro on 24.03.2017 / 19:46
 * DiscordBot / commands
 * © zekro 2017
 */


public class Help implements Command {


    EmbedBuilder eb = new EmbedBuilder();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (args.length > 0) {
            if (Main.commands.containsKey(args[0]))
                if (Main.commands.get(args[0]).help() != null)
                    event.getTextChannel().sendMessage(
                            eb.setColor(new Color(22, 138, 233)).setDescription(Main.commands.get(args[0]).help()).build()
                    ).queue();
                else
                    event.getTextChannel().sendMessage(
                            eb.setColor(Color.red).setDescription(":warning:  There are currently no information for the command  *-" + args[0] + "* !").build()
                    ).queue();
            else
                event.getTextChannel().sendMessage(
                        eb.setColor(Color.red).setDescription(":warning:  The command list does not contains information for the command *-" + args[0] + "* !").build()
                ).queue();
            return;
        }

        event.getMessage().delete().queue();

        Map<String, String> cmds = new TreeMap<>();
        Main.commands.forEach((s, command) -> cmds.put(s, command.description()));
        StringBuilder ciams = new StringBuilder();

        String[] ignorers = {"c", "m", "bj", "ttt", "userinfo", "dev"};
        Arrays.stream(ignorers).forEach(s -> cmds.remove(s));

        //ciams.append("**" + STATICS.CMDTYPE.administration + "**\n");
        //cmds.keySet().stream()
        //        .filter(s -> Main.commands.get(s).commandType().equals(STATICS.CMDTYPE.administration))
        //        .forEach(s1 -> ciams.append(
        //                ":white_small_square:  **" + s1 + "**   -   `" + cmds.get(s1) + "`\n"
        //        ));
        //
        //ciams.append("**" + STATICS.CMDTYPE.chatutils + "**\n");
        //cmds.keySet().stream()
        //        .filter(s -> Main.commands.get(s).commandType().equals(STATICS.CMDTYPE.chatutils))
        //        .forEach(s1 -> ciams.append(
        //                ":white_small_square:  **" + s1 + "**   -   `" + cmds.get(s1) + "`\n"
        //        ));
        //
        //ciams.append("**" + STATICS.CMDTYPE.essentials + "**\n");
        //cmds.keySet().stream()
        //        .filter(s -> Main.commands.get(s).commandType().equals(STATICS.CMDTYPE.essentials))
        //        .forEach(s1 -> ciams.append(
        //                ":white_small_square:  **" + s1 + "**   -   `" + cmds.get(s1) + "`\n"
        //        ));
        //
        //ciams.append("**" + STATICS.CMDTYPE.etc + "**\n");
        //cmds.keySet().stream()
        //        .filter(s -> Main.commands.get(s).commandType().equals(STATICS.CMDTYPE.etc))
        //        .forEach(s1 -> ciams.append(
        //                ":white_small_square:  **" + s1 + "**   -   `" + cmds.get(s1) + "`\n"
        //        ));
        //
        //ciams.append("**" + STATICS.CMDTYPE.guildadmin + "**\n");
        //cmds.keySet().stream()
        //        .filter(s -> Main.commands.get(s).commandType().equals(STATICS.CMDTYPE.guildadmin))
        //        .forEach(s1 -> ciams.append(
        //                ":white_small_square:  **" + s1 + "**   -   `" + cmds.get(s1) + "`\n"
        //        ));
        //
        //ciams.append("**" + STATICS.CMDTYPE.music + "**\n");
        //cmds.keySet().stream()
        //        .filter(s -> Main.commands.get(s).commandType().equals(STATICS.CMDTYPE.music))
        //        .forEach(s1 -> ciams.append(
        //                ":white_small_square:  **" + s1 + "**   -   `" + cmds.get(s1) + "`\n"
        //        ));

        cmds.forEach((s, s2) -> ciams.append(
                ":white_small_square:  **" + s + "**   -   `" + s2 + "`\n"
        ));

        try {

            PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
            pc.sendMessage(
                    ":clipboard:  __**COMMAD LIST**__  :clipboard: \n\n" +
                            "If you want a full list of commands with description, please take a look there:\n" +
                            ":point_right:   **http://zekrosbot.zekro.de**\n\n" +
                            ciams.toString()

            ).queue();


        } catch (Exception e) {
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

    @Override
    public String description() {
        return "\"Ich brauch keine Hilfe...\" :D";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.essentials;
    }
}
