package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

import utils.STATICS;

/**
 * Created by zekro on 09.08.2017 / 17:41
 * DiscordBot.commands.chat
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Count implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        String content = String.join(" ", args);

        if (content.isEmpty()) {

        }

        int chars = content.length();
        int chars_only = content.replace(" ", "").length();
        int words = content.split(" ").length;
        int sentences = content.split("[.!?]").length;

        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.green)
                .setTitle("String Analyzer", null)
                .setDescription("```" + content + "```")
                .addField("", "Characters:\nCharacters (w/o spaces):\nWords:\nSentences:", true)
                .addField("", String.format("`%s`\n`%s`\n`%s`\n`%s`", chars, chars_only, words, sentences), true)
                .build()
        ).queue(message -> event.getMessage().delete().queue());

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USGAGE:\n`-count <string>`";
    }

    @Override
    public String description() {
        return "Count chars, words and sentences of an input string";
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
