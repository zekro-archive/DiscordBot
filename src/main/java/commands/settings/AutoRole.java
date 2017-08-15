package commands.settings;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zekro on 19.05.2017 / 15:29
 * DiscordBot/commands.SettingsCore
 * © zekro 2017
 */

public class AutoRole implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(permission(), event)) return;

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        Arrays.stream(args).forEach(s -> sb.append(s + " "));
        List<Role> autoRole = event.getGuild().getRolesByName(sb.toString().substring(0, sb.length() - 1), true);

        if (event.getMessage().getMentionedRoles().size() > 0) {
            SSSS.setAUTOROLE(event.getMessage().getMentionedRoles().get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set autorole to `" + event.getMessage().getMentionedRoles().get(0).getName() + "`.").build()).queue();
        } else if (autoRole.size() > 0) {
            SSSS.setAUTOROLE(autoRole.get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set autorole to `" + autoRole.get(0).getName() + "`.").build()).queue();
        } else {
            SSSS.setAUTOROLE("", event.getGuild());
            event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully deactivated autorole.").build()).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -autorole <role name or role as mention>\n*Hint: Set role to a role that does not exist on the server to disable this function.*";
    }

    @Override
    public String description() {
        return "Set the role members get automatically assigned by joining the server.";
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
