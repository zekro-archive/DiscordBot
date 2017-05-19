package commands.settings;

import commands.Command;
import core.Perms;
import core.SSSS;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by zekro on 19.05.2017 / 11:32
 * DiscordBot/commands.settings
 * © zekro 2017
 */

public class AutoRole implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (Perms.check(2, event)) return;

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
            return;
        }

        if (event.getMessage().getMentionedRoles().size() > 0) {
            SSSS.setAUTOROLE(event.getMessage().getMentionedRoles().get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(MSGS.success.setDescription("Successfully set autorole to role `" + event.getMessage().getMentionedRoles().get(0).getName() + "`.").build()).queue();
        } else {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(args).forEach(s -> sb.append(s + " "));
            SSSS.setAUTOROLE(sb.substring(0, sb.toString().length() - 1), event.getGuild());
            if (event.getGuild().getRolesByName(sb.substring(0, sb.toString().length() - 1), true).size() > 0)
                event.getTextChannel().sendMessage(MSGS.success.setDescription("Successfully set autorole to role `" + sb.substring(0, sb.toString().length() - 1) + "`.").build()).queue();
            else
                event.getTextChannel().sendMessage(MSGS.success.setDescription("Successfully deactivated autorole.").build()).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -autorole <Role as Mention or role name>\n*Hint: Set it to a role that does not exist to disable autorole.*";
    }

    @Override
    public String description() {
        return "Set the role that new members automatically get assigned by the bot.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.settings;
    }
}
