package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by zekro on 09.09.2017 / 20:42
 * DiscordBot.commands.etc
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Donate implements Command {

    private static final String LIST_URL = "https://pastebin.com/raw/pLb3YcW2";
    private static final float NEED_VAL = 20;

    private float allval = 0;


    private void addAllval(float add) {
        allval += add;
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        allval = 0;

        DecimalFormat df = new DecimalFormat("###.##");
        URL url = new URL(LIST_URL);
        Scanner s = new Scanner(url.openStream());
        HashMap<String, Float> donators = new HashMap<>();
        while (s.hasNextLine()) {
            String[] l = s.nextLine().replace("\n", "").split(",");
            donators.put(l[0], Float.parseFloat(l[1]));
        }

        StringBuilder sb = new StringBuilder();

        donators.forEach((n, v) -> {
            sb.append(String.format(":white_small_square:  **%s**   -   %s €\n", n, df.format(v)));
            addAllval(v);
        });

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0xFFD200))
                .setDescription(
                        "This bot is currently running on an virtual server, witch I (zekro) rent for around 20 € every year.\n" +
                        "Sure, this bot is fully free to use without any costs, but I would really appreciate if you could help me" +
                        "to finance this costs of the server by donations. :hearts:\n\n" +
                        "If you want to donate, you can do it **[here](https://www.paypal.me/zekro)** *(with PayPal)*.\n\n\n" +
                        "**List of donator's** *(for current year cycle)*\n\n" + sb.toString() + "\n\n" +
                        String.format(":trophy:  Goal:  **%s € / %s €**", df.format(allval), df.format(NEED_VAL))
                );

        event.getTextChannel().sendMessage(eb.build()).queue();
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
        return "Get information about donating";
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
