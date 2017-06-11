package commands.administration;

import commands.Command;
import core.Perms;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zekro on 11.06.2017 / 10:10
 * DiscordBot/commands.administration
 * © zekro 2017
 */

public class Broadcast implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        if (args.length < 1) {
            return;
        }

        List<String> ignoreList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        Arrays.stream(args).filter(s -> s.startsWith("-")).forEach(s -> ignoreList.add(s.replaceFirst("-", "")));
        Arrays.stream(args).filter(s -> !ignoreList.contains(s.replaceFirst("-", ""))).forEach(s -> sb.append(" " + s));

        event.getJDA().getGuilds().stream()
                .filter(g -> !ignoreList.contains(g.getId()))
                .forEach(g -> g.getPublicChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.cyan)
                                .setAuthor("\uD83D\uDCE2  Broadcast message by zekro", null, event.getAuthor().getAvatarUrl())
                                .setDescription(sb.toString().substring(1))
                                .build()
                ).queue());

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
}
