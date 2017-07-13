package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zekro on 03.05.2017 / 21:52
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class Dev implements Command {

    String[] roles = {
            "java",
            "javascript",
            "c#",
            "c++",
            "c",
            "python",
            "lua",
            "scala",
            "html",
            "css",
            ".net",
            "ruby",
            "php",
            "perl",
            "go",
            "vb"
    };

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        Guild g = event.getGuild();

        if (!g.getId().equals("307084334198816769"))
            return;

        StringBuilder langs = new StringBuilder();

        Arrays.stream(roles).forEach(s -> langs.append("-  " + s + "\n"));

        if (args.length < 1) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Color.cyan)
                    .addField("Usage:", "-dev <language> <language2> <language3> ...", false)
                    .addField("Languages", langs.toString(), false);
            event.getTextChannel().sendMessage(eb.build()).queue();
        } else {

            try {

                ArrayList<Role> rolesToAdd = new ArrayList<>();

                Arrays.stream(args).forEach(s -> rolesToAdd.add(g.getRolesByName(s, true).get(0)));

                System.out.println(rolesToAdd.size());

                g.getController().addRolesToMember(event.getMember(), rolesToAdd).queue();

                StringBuilder sb = new StringBuilder();
                rolesToAdd.forEach(role -> sb.append(role.getName() + ", "));

                event.getTextChannel().sendMessage(
                        new EmbedBuilder().setColor(Color.green).setDescription(event.getAuthor().getAsMention() + ", you got the following roles: \n\n" + sb.toString().substring(0, sb.toString().length() - 1)).build()
                ).queue();

            } catch (Exception e) {
                e.printStackTrace();
                event.getTextChannel().sendMessage(
                        new EmbedBuilder().setColor(Color.red).setDescription("Please enter valid role names that are listed! Use *-dev* to get the language list. If you want a special language added, please contact a Moderator or Admin to add you the special role.").build()
                ).queue();
            }

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
        return null;
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }

    @Override
    public int permission() {
        return 0;
    }
}
