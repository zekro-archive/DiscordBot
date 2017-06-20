package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zekro on 20.06.2017 / 09:59
 * DiscordBot.commands.chat
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */


public class WarframeAlerts implements Command {


    private static final String ALERTS_API = "https://deathsnacks.com/wf/data/alerts_raw.txt";
    private static Timer timer;
    private ArrayList<String> lastList;

    public class AlertsParser {

        String ID;
        String location;
        String type;
        String enemies;
        int minLVL;
        int maxLVL;
        long start;
        long end;
        String[] loot;

        public AlertsParser(String input) {

            // 5948d0d9f6f6200694b7807b|Ani|Void|Sabotage|Corrupted|23|28|1497944555|1497946714|9,800cr - Cierzo Zephyr Helmet Blueprint|
            //      [0]                 [1]  [2]  [3]       [4]     [5][6]   [7]       [8]               [9]

            String[] split = input.split("\\|");

            this.ID         = split[0];
            this.location   = split[1] + " (" + split[2] + ")";
            this.type       = split[3];
            this.enemies    = split[4];
            this.minLVL     = Integer.parseInt(split[5]);
            this.maxLVL     = Integer.parseInt(split[6]);
            this.start      = Long.parseLong(split[7]);
            this.end        = Long.parseLong(split[8]);
            this.loot       = split[9].split(" - ");

        }

    }

    private ArrayList<String> getAlertsRaw() throws IOException {

        Scanner sc = new Scanner(new URL(ALERTS_API).openStream());
        ArrayList<String> out = new ArrayList<>();

        while (sc.hasNextLine()) {
            out.add(sc.nextLine());
        }

        return out;
    }

    private ArrayList<AlertsParser> getAlerts() throws IOException {

        ArrayList<AlertsParser> out = new ArrayList<>();
        getAlertsRaw().forEach(s -> out.add(new AlertsParser(s)));

        return out;
    }

    private String getCurrentTime() {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss - MM/dd/yyyy");

        return format.format(date);
    }

    private String getStartEndTime(long start, long end) {

        Date date = new Date();
        String out;
        long time;

        if (start > Math.floorDiv(date.getTime(), 1000)) {
            out = "Staring in ";
            time = start;
        } else {
            out = "Ending in ";
            time = end;
        }

        long seconds = Math.abs(time - Math.floorDiv(date.getTime(), 1000));
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);

        return out + hours + "h, " + mins + "m, " + seconds + "s";

    }

    private EmbedBuilder createMessage(ArrayList<AlertsParser> inputList) {

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0x0088FF))
                .setTitle("WARFRAME ALERTS", null)
                .setDescription("*[ " + getCurrentTime() + " ]*\n\n_____");

        inputList.forEach(alert -> eb.addField(
                alert.location,
                "**TYPE:**   " + alert.type + "\n" +
                       "**ENEMIES:**   " + alert.enemies + "\n" +
                       "**LEVEL:**   " + alert.minLVL + " - " + alert.maxLVL + "\n" +
                       "**LOOT:**   " + Arrays.stream(alert.loot).collect(Collectors.joining(" - ")).replace(",", ".") + "\n" +
                       "**STATUS:**   " + getStartEndTime(alert.start, alert.end) + "\n" +
                       "___________",
                false
        ));

        return eb;

    }

    private boolean hasUpdated() throws IOException {

        return lastList.size() > 0 && !lastList.equals(getAlertsRaw());

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (args.length < 1) {

            // TODO: Clear Test Outputs
            System.out.println(
            );

            event.getTextChannel().sendMessage(createMessage(getAlerts()).build()).queue();

            return;
        }

        switch (args[0].toLowerCase()) {

            case "":

        }

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
        return STATICS.CMDTYPE.chatutils;
    }
}
