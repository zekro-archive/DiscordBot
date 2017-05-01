package commands.essentials;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;

public class Info implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.MAGENTA)
                        .setDescription(
                            "**zekroBot** JDA Discord Bot - v." + STATICS.VERSION + "\n\n" +
                            "Webpage: http://dev.zekro.de\n" +
                            "Readme/Changelogs: http://github.zekro.de/DiscordBot/blob/master/README.md\n" +
                            "Github Repository: http://github.zekro.de/DiscordBot\n\n" +
                            "*© 2017 zekro*"
                        )
                        .setThumbnail("https://dl.dropboxusercontent.com/s/clxgmgaon7o6pkh/official_avatar.jpg")
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
