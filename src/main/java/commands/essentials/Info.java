package commands.essentials;

import commands.Command;
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

            URL url = new URL(core.update.versionURL);
            Scanner s = new Scanner(url.openStream());
            version = s.nextLine();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.MAGENTA)
                        .setDescription(":robot:   __**zekroBot** JDA Discord Bot__")
                        .addField("Current Version", STATICS.VERSION, true)
                        .addField("Latest Version", version, true)
                        //.addField("Release", STATICS.BUILDTYPE.STABLE, true)
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
                        .addField("Bugreporting",
                                "If you got some bugs, please contact us here:\n" +
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
        return null;
    }

    @Override
    public String description() {
        return "Get info about that bot";
    }
}
