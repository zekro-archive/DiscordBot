package commands.etc;

import com.moandjiezana.toml.TomlWriter;
import commands.Command;
import core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.text.ParseException;

/**
 * Created by zekro on 05.07.2017 / 20:53
 * DiscordBot.commands.etc
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */


public class BotStats implements Command {

    static File f = new File("botstatics.donotdelete");

    public static long messagesProcessed = 0;
    public static long commandsExecuted = 0;

    private int membersDeserving = 0;

    public static void save() {

        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(messagesProcessed + "\n" + commandsExecuted);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static long[] stats() {

        if (!f.exists())
            return new long[] {0, 0};

        try {

            BufferedReader br = new BufferedReader(new FileReader(f));
            return new long[] {Long.parseLong(br.readLine()), Long.parseLong(br.readLine())};

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new long[] {0, 0};
    }

    public static void load() {
        messagesProcessed = stats()[0];
        commandsExecuted = stats()[1];
    }

    private void countUpMembers() {
        membersDeserving++;
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        int commandsSize = Main.commands.size();
        int serversRunning = event.getJDA().getGuilds().size();

        event.getJDA().getGuilds().forEach(g -> g.getMembers().forEach(m -> countUpMembers()));

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                .setColor(Color.cyan)
                .setDescription(
                        "**zekroBot STATS**\n\n" +
                        "```\n" +
                        "Registered commands:  " + commandsSize + "\n" +
                        "Running on servers:   " + serversRunning + " / " + STATICS.SERVER_LIMIT + "\n" +
                        "Deserving members:    " + membersDeserving + "\n" +
                        "Messages processed:   " + messagesProcessed + "\n" +
                        "Commands executed:    " + commandsExecuted + "\n" +
                        "```"
                ).build()
        ).queue();

        membersDeserving = 0;

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:**\n`-botstats`";
    }

    @Override
    public String description() {
        return "Displays stats of the bot.";
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
