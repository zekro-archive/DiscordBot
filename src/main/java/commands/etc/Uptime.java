package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zekro on 14.05.2017 / 10:34
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class Uptime implements Command {


    private String getTime(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private String getTimeDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays + " d, " + parseTimeNumbs(diffHours) + " h, " + parseTimeNumbs(diffMinutes) + " min, " + parseTimeNumbs(diffSeconds) + " sec";
    }

    private String parseTimeNumbs(long time) {
        String timeString = time + "";
        if (timeString.length() < 2)
            timeString = "0" + time;
        return timeString;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(new Color(255, 71,0))
                        .setDescription(":alarm_clock:   **UPTIME**")
                        .addField("Last restart", getTime(STATICS.lastRestart, "dd.MM.yyyy - HH:mm:ss (z)"), false)
                        .addField("Online since", getTimeDiff(new Date(), STATICS.lastRestart), false)
                        .addField("Reconnects since last restart", STATICS.reconnectCount + "", false)
                        .build()
        ).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -uptime";
    }

    @Override
    public String description() {
        return "Get stats about the uptime of the bot.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }

    @Override
    public int permission() {
        return 0;
    }
}
