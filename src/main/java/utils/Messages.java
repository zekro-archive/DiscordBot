package utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.Color;
import java.util.HashMap;

/**
 * Created by zekro on 12.08.2017 / 14:23
 * DiscordBot.utils
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */


public class Messages {

    public static Message error(TextChannel chan, String content) {
        return chan.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(content).build()).complete();
    }

    public static Message error(TextChannel chan, String content, String title) {
        return chan.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(content).setTitle(title, null).build()).complete();
    }

    public static Message error(TextChannel chan, String content, String title, HashMap<String, String> fields) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.red).setDescription(content).setTitle(title, null);
        fields.forEach((t, c) -> embed.addField(t, c, false));
        return chan.sendMessage(embed.build()).complete();
    }

    public static Message message(TextChannel chan, String content) {
        return chan.sendMessage(new EmbedBuilder().setDescription(content).build()).complete();
    }

    public static Message message(TextChannel chan, String content, Color color) {
        return chan.sendMessage(new EmbedBuilder().setColor(color).setDescription(content).build()).complete();
    }

    public static Message message(TextChannel chan, String content, String title) {
        return chan.sendMessage(new EmbedBuilder().setDescription(content).setTitle(title, null).build()).complete();
    }

    public static Message message(TextChannel chan, String content, String title, Color color) {
        return chan.sendMessage(new EmbedBuilder().setDescription(content).setColor(color).setTitle(title, null).build()).complete();
    }

    public static Message message(TextChannel chan, String content, String title, HashMap<String, String> fields, Color color) {
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color).setTitle(title, null);
        fields.forEach((t, c) -> embed.addField(t, c, false));
        return chan.sendMessage(embed.build()).complete();
    }

}
