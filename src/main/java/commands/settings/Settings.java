package commands.settings;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by zekro on 30.05.2017 / 11:00
 * DiscordBot/commands.Settings
 * © zekro 2017
 */

public class Settings implements Command {

    //private void resetSettings(Guild g) {
    //    File[] files = new File("SERVER_SETTINGS/" + g.getId() + "/").listFiles();
//
    //    Arrays.stream(files).forEach(f -> {
    //        try {
    //            FileDeleteStrategy.FORCE.delete(f);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    });
    //}


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(1, event)) return;
        core.SSSS.listSettings(event);

        // THIS SHIT DOES NOT WORK
        //if (args.length < 1) {
        //    if (core.Perms.check(1, event)) return;
        //    core.SSSS.listSettings(event);
        //} else if (args[0].equals("reset")) {
        //    if (core.Perms.check(3, event)) return;
        //    File path = new File("SERVER_SETTINGS/" + event.getGuild().getId());
        //    if (path.exists()) {
        //        resetSettings(event.getGuild());
        //        event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully deleted guild specific settings for this server!").build()).queue();
        //    }
        //    else
        //        event.getTextChannel().sendMessage(MSGS.error().setDescription("No server specific settings found for this guild!").build()).queue();
        //}
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USING:\n" +
               "**SettingsCore**  -  `List all current SettingsCore values of the current guild`";
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
