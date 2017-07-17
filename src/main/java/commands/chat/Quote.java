package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zekro on 05.06.2017 / 10:17
 * DiscordBot/commands.chat
 * © zekro 2017
 */

public class Quote implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        event.getMessage().delete().queue();

        Message chanMSG = event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Searching for message in text channels...").build()).complete();

        List<Message> msg = new ArrayList<>();
        event.getGuild().getTextChannels().forEach(c -> {
            try {
                msg.add(c.getMessageById(args[0]).complete());
            } catch (Exception e) {}
        });

        if (msg.size() < 1) {
            chanMSG.editMessage(MSGS.error().setDescription(
                    "There is no message in any chat on this guild with the ID `" + args[0] + "`."
            ).build()).queue();
            return;
        }

        chanMSG.editMessage(new EmbedBuilder()
                .setAuthor(msg.get(0).getAuthor().getName(), null, msg.get(0).getAuthor().getAvatarUrl())
                .setDescription(msg.get(0).getContent())
                .setFooter(
                        msg.get(0).getCreationTime().getDayOfMonth() + ". " +
                                msg.get(0).getCreationTime().getMonth() + " " +
                                msg.get(0).getCreationTime().getYear() +
                                " at " + msg.get(0).getCreationTime().getHour() + ":" +
                                msg.get(0).getCreationTime().getMinute() + ":" +
                                msg.get(0).getCreationTime().getSecond() +
                                " in channel #" + msg.get(0).getTextChannel().getName(),
                        null)
                .build()
        ).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:** `-quote <msg id>`";
    }

    @Override
    public String description() {
        return "Quote a message in any channel on guild";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
    }

    @Override
    public int permission() {
        return 0;
    }
}
