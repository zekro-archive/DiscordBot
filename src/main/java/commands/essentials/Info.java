package commands.essentials;

import commands.Command;
import core.UpdateClient;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Info implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        String version = "NOT AVAILABLE";
        try {

            version = UpdateClient.PRE.tag;

        } catch (Exception e) {
            e.printStackTrace();
        }

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.MAGENTA)
                        .setThumbnail("https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/zekroBot%20Logo%20-%20round.png")
                        .setDescription(":robot:   __**zekroBot** JDA Discord Bot__")
                        .addField("Current Version", STATICS.VERSION, true)
                        .addField("Latest Version", version, true)
                        .addField("Copyright",
                                "Coded by zekro Development Team.\n" +
                                      "© 2016 - 2017 Ringo Hoffmann and Sophie Lorenz.", false)
                        .addField("Information and Links",
                                "GitHub Repository: \n*http://github.zekro.de/DiscordBot*\n\n" +
                                      "Readme/Changelogs: \n*http://github.zekro.de/DiscordBot/blob/master/README.md*\n\n" +
                                      "Webpage: \n*http://dev.zekro.de*\n\n" +
                                      "Github Profile: \n*http://github.zekro.de*", false)
                        .addField("Libraries and Dependencies",
                                " -  JDA  *(https://github.com/DV8FromTheWorld/JDA)*\n" +
                                      " -  Toml4J  *(https://github.com/mwanji/toml4j)*\n" +
                                      " -  lavaplayer  *(https://github.com/sedmelluq/lavaplayer)*\n" +
                                      " -  Steam-Condenser  *(https://github.com/koraktor/steam-condenser-java)*", false)
                        .addField("Bug Reporting / Idea Suggestion",
                                "If you got some bugs, please contact us here:\n" +
                                      " - **Please use this document to report a Bug or suggest an idea: http://s.zekro.de/botsubs**\n" +
                                      " -  E-Mail:  zekrotja@gmail.com\n" +
                                      " -  Discord:  http://discord.zekro.de (or directly: `zekro#9131` & `Sophie#4538`)", false)


                            //"Webpage: http://dev.zekro.de\n" +
                            //"Readme/Changelogs: http://github.zekro.de/DiscordBot/blob/master/README.md\n" +
                            //"Github Repository: http://github.zekro.de/DiscordBot\n\n" +
                            //"*© 2017 zekro*"

                        //.setThumbnail("https://dl.dropboxusercontent.com/s/clxgmgaon7o6pkh/official_avatar.jpg")
                        .build()

        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -info";
    }

    @Override
    public String description() {
        return "Get info about that bot";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.essentials;
    }

    @Override
    public int permission() {
        return 0;
    }
}
