package utils;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

/**
 * Created by zekro on 04.05.2017 / 12:09
 * DiscordBot/utils
 * © zekro 2017
 */

public class MSGS {

    public static EmbedBuilder success() {
        return new EmbedBuilder().setColor(Color.green);
    }

    public static EmbedBuilder error() {
        return new EmbedBuilder().setColor(Color.red);
    }

}
