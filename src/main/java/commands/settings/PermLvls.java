package commands.settings;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

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

        if (args.length < 2) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        Arrays.asList(args).subList(1, args.length).forEach(s -> sb.append(s + " "));

        switch (args[0]) {

            case "1":
                SSSS.setPERMROLES_1(sb.toString().substring(0, sb.toString().length() - 1), event.getGuild());
                event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set roles `" + sb.toString().substring(0, sb.toString().length() - 1) + "` to permission level `" + args[0] + "`").build()).queue();
                break;

            case "2":
                SSSS.setPERMROLES_2(sb.toString().substring(0, sb.toString().length() - 1), event.getGuild());
                event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set roles `" + sb.toString().substring(0, sb.toString().length() - 1) + "` to permission level `" + args[0] + "`").build()).queue();
                break;

            default:
                event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
                break;

        }

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
