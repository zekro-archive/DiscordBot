package commands.settings;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zekro on 30.05.2017 / 11:00
 * DiscordBot/commands.Settings
 * © zekro 2017
 */

public class Settings implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {
        if (core.Perms.check(1, event)) return;
        core.SSSS.listSettings(event);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USING:\n" +
               "**Settings**  -  `List all current Settings values of the current guild`";
    }

    @Override
    public String description() {
        return "List all current Settings values of the current guild";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.settings;
    }

    @Override
    public int permission() {
        return 0;
    }
}
