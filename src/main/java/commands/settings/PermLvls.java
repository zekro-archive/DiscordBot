package commands.settings;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zekro on 17.05.2017 / 20:06
 * DiscordBot/commands.SettingsCore
 * © zekro 2017
 */

public class PermLvls implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(permission(), event)) return;

        TextChannel tc = event.getTextChannel();
        Guild guild = event.getGuild();

        if (args.length < 2) {
            tc.sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        List<String> inputroles = Arrays.asList(Arrays.stream(args).skip(1).collect(Collectors.joining("")).replace(", ", ",").split(","));
        List<Role> roles = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        inputroles.forEach(ir -> {
            Role role = null;
            try {
                role = guild.getRoleById(ir);
            } catch (Exception e) {}
            if (role == null)
                role = guild.getRolesByName(ir, true).size() > 0 ? guild.getRolesByName(ir, true).get(0) : null;
            if (role == null)
                failed.add(ir);
            else
                roles.add(role);
        });


        String injection = roles.stream().map(r -> r.getId()).collect(Collectors.joining(","));

        switch (args[0]) {

            case "1":
                SSSS.setPERMROLES_1(injection, guild);
                break;
            case "2":
                SSSS.setPERMROLES_2(injection, guild);
                break;

            default:
                tc.sendMessage(MSGS.error().setDescription(help()).build()).queue();
                return;

        }

        if (roles.size() > 0)
            tc.sendMessage(MSGS.success().setDescription(String.format(
                "Set role(s) ```%s``` for permission level **%s**", injection.replace(",", ", "), args[0]
            )).build()).queue();

        if (failed.size() > 0)
            tc.sendMessage(MSGS.error().setDescription(String.format(
                    "Failed to parse following roles: ```%s``` Please enter valid role names or ID's", failed.stream().collect(Collectors.joining(", "))
            )).build()).queue();

        //if (core.Perms.check(permission(), event)) return;
//
        //if (args.length < 2) {
        //    event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
        //    return;
        //}
//
        //StringBuilder sb = new StringBuilder();
        //Arrays.asList(args).subList(1, args.length).forEach(s -> sb.append(s + " "));
//
        //switch (args[0]) {
//
        //    case "1":
        //        SSSS.setPERMROLES_1(sb.toString().substring(0, sb.toString().length() - 1), event.getGuild());
        //        event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set roles `" + sb.toString().substring(0, sb.toString().length() - 1) + "` to permission level `" + args[0] + "`").build()).queue();
        //        break;
//
        //    case "2":
        //        SSSS.setPERMROLES_2(sb.toString().substring(0, sb.toString().length() - 1), event.getGuild());
        //        event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set roles `" + sb.toString().substring(0, sb.toString().length() - 1) + "` to permission level `" + args[0] + "`").build()).queue();
        //        break;
//
        //    default:
        //        event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
        //        break;
//
        //}

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -permlvl <lvl 1/2> <role1>, <role2>, ...";
    }

    @Override
    public String description() {
        return "Set roles for permission levels.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.settings;
    }

    @Override
    public int permission() {
        return 3;
    }
}
