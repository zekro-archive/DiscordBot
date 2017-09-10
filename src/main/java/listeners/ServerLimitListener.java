package listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.awt.Color;
import java.io.*;
import java.util.*;

/**
 * Created by zekro on 19.08.2017 / 11:53
 * DiscordBot.listeners
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */


public class ServerLimitListener extends ListenerAdapter {

    private Timer timer = new Timer();

    private List<String> wildcards;


    private List<String> getWilrdcards() {

        List<String> out = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("WILDCARDS.txt"));
            br.lines().forEach(s -> {
                if (!s.startsWith("#") && !s.isEmpty())
                    out.add(s.replace("\n", ""));
            });
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private void updateWildcards(String usedone) {
        List<String> cards = getWilrdcards();
        cards.remove(usedone);
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter("WILDCARDS.txt"));
            cards.forEach(s -> {
                try {
                    br.write(s + "\n");
                } catch (IOException e) {}
            });
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String generateToken() {
        final String tokenchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb = new StringBuilder().append("token_");
        for (int i = 0; i <= 8; i++)
            sb.append(tokenchars.charAt(new Random().nextInt(tokenchars.length())));
        return sb.toString();
    }

    public static void createTokenList(int ammount) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("WILDCARDS.txt"));
            bw.write("# Here you can add or remove tokens. \n# Just empty the file if you don't want to enable token system.\n\n");
            for (int i = 0; i <= ammount; i++)
                bw.write(generateToken() + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        Guild guild = event.getGuild();
        wildcards = getWilrdcards();

        if (guild.getJDA().getGuilds().size() > STATICS.SERVER_LIMIT) {
            guild.getOwner().getUser().openPrivateChannel().queue(pc -> pc.sendMessage(
                    new EmbedBuilder().setColor(Color.ORANGE).setTitle("Server Limit Reached", null).setDescription(
                            "Sorry, but because of currently limited resources the bot is limited to run on maximum " + STATICS.SERVER_LIMIT + " Servers.\n\n" +
                            "If you have a wildcard, you can send the token via PM to zekroBot.\n" +
                            "Otherwise, the bot will leave the server automatically after 2 minutes."
                    ).build()
            ).queue());
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    guild.leave().queue();
                }
            }, 120000);
        }

    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        String content = event.getMessage().getContent();
        Message msg = event.getMessage();

        if (content.startsWith("token_")) {

            String entered = content.replaceAll(" ", "");
            if (wildcards.contains(entered)) {
                timer.cancel();
                timer = new Timer();
                updateWildcards(entered);
                msg.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder().setColor(Color.green).setDescription(
                        "Access granted!\n" +
                        "You'll now have access to all functions of this bot.\n\n" +
                        "ATTENTION: If you kick the bot from your guild, you will need a new token and the old one got invalid!"
                ).build()).queue();
            } else {
                msg.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                        "The entered token is invalid!\n\n" +
                        "Try to re-enter the token. The bot will be kicked automatically after time has expired."
                ).build()).queue();
            }

        }
    }

}
