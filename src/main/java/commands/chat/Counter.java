package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import utils.Messages;
import utils.STATICS;

/**
 * Created by zekro on 26.08.2017 / 11:47
 * DiscordBot.commands.chat
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */


public class Counter implements Command, Serializable {

    private static HashMap<Guild, List<CCounter>> counters = new HashMap<>();


    private class CCounter implements Serializable {
        private static final long serialVersionUID = -4410308390500314827L;
        private String name;
        private String creator;
        private int count;

        private CCounter(String name, Member creator) {
            this.name = name;
            this.creator = creator.getUser().getId();
            this.count = 0;
        }

        private Member getCreator(Guild guild) {
            return guild.getMemberById(creator);
        }
    }


    private void create(String[] args, MessageReceivedEvent event) {
        Guild g = event.getGuild();

        String name = String.join(" ", Arrays.asList(args).subList(1, args.length));
        CCounter count = new CCounter(name, event.getMember());

        if (args.length < 2) {
            Messages.message(event.getTextChannel(), help(), Color.red);
            return;
        }

        if (counters.containsKey(g))
            counters.get(g).add(count);
        else {
            List<CCounter> l = new ArrayList<>();
            l.add(count);
            counters.put(event.getGuild(), l);
        }

        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green).setDescription(
                String.format("Successfully created counter `%s`. *(ID: %d)*", name, counters.get(g).size() - 1)
        ).build()).queue();
    }

    private void changeValue(String[] args, MessageReceivedEvent event) {
        Guild g = event.getGuild();

        if (!counters.containsKey(event.getGuild())) {
            Messages.message(event.getTextChannel(), "This guild has no counters!", Color.red);
            return;
        }

        int id;

        try {
            id = Integer.valueOf(args[0]);
            if (id > counters.get(g).size() - 1)
                throw new Exception();
        } catch (Exception e) {
            Messages.message(event.getTextChannel(), "Please enter a valid ID.", Color.red);
            return;
        }

        if (args.length == 2) {
            int ammount = Integer.valueOf(args[1]);
            CCounter tcount = counters.get(event.getGuild()).get(id);
            tcount.count += ammount;
            counters.get(event.getGuild()).remove(id);
            counters.get(event.getGuild()).add(id, tcount);
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green).setDescription(
                String.format("Changed value from counter `%s` to **%d**.", tcount.name, tcount.count)
            ).build()).queue();
        } else {
            CCounter tcount = counters.get(g).get(id);
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green).setDescription(
                    String.format("`[%d]`  -  %s  -  **%d**", id, tcount.name, tcount.count)
            ).build()).queue();
        }
    }

    private void delete(String[] args, MessageReceivedEvent event) {
        Guild g = event.getGuild();

        if (!counters.containsKey(g)) {
            Messages.message(event.getTextChannel(), "This guild has no counters!", Color.red);
            return;
        }

        int id;

        try {
            id = Integer.valueOf(args[1]);
            if (id > counters.get(g).size() - 1)
                throw new Exception();
        } catch (Exception e) {
            Messages.message(event.getTextChannel(), "Please enter a valid ID.", Color.red);
            return;
        }

        Member creator = counters.get(g).get(id).getCreator(g);
        if (!creator.equals(event.getMember())) {
            Messages.error(event.getTextChannel(), String.format("Only the creator of the counter (%s) is allowed to delete this counter.", creator.getAsMention()));
            return;
        }

        CCounter tcount = counters.get(g).get(id);
        counters.get(g).remove(id);

        new File("SERVER_SETTINGS/" + g.getId() + "/counters/counter_" + id + ".dat").delete();

        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(
                String.format("Successfully removed counter `%s` *(%d)*.", tcount.name, id)
        ).build()).queue();
    }

    private void list(MessageReceivedEvent event) {
        Guild g = event.getGuild();

        if (!counters.containsKey(event.getGuild())) {
            Messages.message(event.getTextChannel(), "This guild has no counters!", Color.red);
            return;
        }

        StringBuilder sb = new StringBuilder();
        AtomicInteger count = new AtomicInteger();
        counters.get(g).forEach(c ->
            sb.append(String.format(":white_small_square:   `[%d]`  -  %s  -  **%d**\n", count.getAndAdd(1), c.name, c.count))
        );
        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.cyan)
                .setTitle("Current counters")
                .setDescription(sb.toString())
                .build()).queue();
    }

    private static void saveAll() throws IOException {

        counters.forEach((g, l) -> {

            File path = new File("SERVER_SETTINGS/" + g.getId() + "/counters");
            if (!path.exists())
                path.mkdirs();

            AtomicInteger count = new AtomicInteger();
            l.forEach(c -> {
                try {
                    FileOutputStream fos = new FileOutputStream("SERVER_SETTINGS/" + g.getId() + "/counters/counter_" + count.getAndAdd(1) + ".dat");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(c);
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public static void loadAll(JDA jda) {

        jda.getGuilds().forEach(g -> {

            File path = new File("SERVER_SETTINGS/" + g.getId() + "/counters");

            if (!path.exists())
                path.mkdirs();

            File[] files = path.listFiles();
            if (files.length < 1)
                return;

            List<CCounter> counts = new ArrayList<>();
            Arrays.stream(files).forEach(f -> {
                try {
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    CCounter out = (CCounter) ois.readObject();
                    counts.add(out);
                    ois.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            counters.put(g, counts);
        });
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        Message message = event.getMessage();
        Guild guild = event.getGuild();
        Member author = event.getMember();

        if (args.length < 1) {
            Messages.message(message.getTextChannel(), help(), Color.red);
            return;
        }

        switch (args[0]) {

            case "add":
            case "create":
                create(args, event);
                break;

            case "delete":
            case "remove":
                delete(args, event);
                break;

            case "list":
                list(event);
                break;

            default:
                changeValue(args, event);
        }

        saveAll();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return  "**USAGE:**\n" +
                ":white_small_square:   `-counter add <name>`  -  Add counter\n" +
                ":white_small_square:   `-counter <id>`  -  Display counter\n" +
                ":white_small_square:   `-counter <id> <change>`  -  Change counter value\n" +
                ":white_small_square:   `-counter list`  -  List all counters\n" +
                ":white_small_square:   `-counter remove <id>`  -  Remove counter\n";
    }

    @Override
    public String description() {
        return "Create counters";
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
