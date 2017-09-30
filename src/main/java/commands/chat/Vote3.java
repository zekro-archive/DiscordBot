package commands.chat;

import com.sun.org.apache.xml.internal.serializer.SerializerTrace;
import commands.Command;
import core.Perms;
import core.SSSS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by zekro on 13.08.2017 / 11:54
 * DiscordBot.commands.chat
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Vote3 implements Command, Serializable {

    private static HashMap<Guild, Message> tempList = new HashMap<>();

    private static TextChannel channel;
    public static HashMap<Guild, Poll> polls = new HashMap<>();
    private static final String[] EMOTI = ( "\uD83C\uDF4F \uD83C\uDF4E \uD83C\uDF50 \uD83C\uDF4A \uD83C\uDF4B \uD83C\uDF4C \uD83C\uDF49 \uD83C\uDF47 \uD83C\uDF53 \uD83C\uDF48 \uD83C\uDF52 \uD83C\uDF51 \uD83C\uDF4D \uD83E\uDD5D " +
                                            "\uD83E\uDD51 \uD83C\uDF45 \uD83C\uDF46 \uD83E\uDD52 \uD83E\uDD55 \uD83C\uDF3D \uD83C\uDF36 \uD83E\uDD54 \uD83C\uDF60 \uD83C\uDF30 \uD83E\uDD5C \uD83C\uDF6F \uD83E\uDD50 \uD83C\uDF5E " +
                                            "\uD83E\uDD56 \uD83E\uDDC0 \uD83E\uDD5A \uD83C\uDF73 \uD83E\uDD53 \uD83E\uDD5E \uD83C\uDF64 \uD83C\uDF57 \uD83C\uDF56 \uD83C\uDF55 \uD83C\uDF2D \uD83C\uDF54 \uD83C\uDF5F \uD83E\uDD59 " +
                                            "\uD83C\uDF2E \uD83C\uDF2F \uD83E\uDD57 \uD83E\uDD58 \uD83C\uDF5D \uD83C\uDF5C \uD83C\uDF72 \uD83C\uDF65 \uD83C\uDF63 \uD83C\uDF71 \uD83C\uDF5B \uD83C\uDF5A \uD83C\uDF59 \uD83C\uDF58 " +
                                            "\uD83C\uDF62 \uD83C\uDF61 \uD83C\uDF67 \uD83C\uDF68 \uD83C\uDF66 \uD83C\uDF70 \uD83C\uDF82 \uD83C\uDF6E \uD83C\uDF6D \uD83C\uDF6C \uD83C\uDF6B \uD83C\uDF7F \uD83C\uDF69 \uD83C\uDF6A \uD83E\uDD5B " +
                                            "\uD83C\uDF75 \uD83C\uDF76 \uD83C\uDF7A \uD83C\uDF7B \uD83E\uDD42 \uD83C\uDF77 \uD83E\uDD43 \uD83C\uDF78 \uD83C\uDF79 \uD83C\uDF7E \uD83E\uDD44 \uD83C\uDF74 \uD83C\uDF7D").split(" ");


    public class Poll implements Serializable {

        private static final long serialVersionUID = -4410308390500314827L;
        private String creator;
        private String heading;
        private List<String> answers;
        private List<String> emotis;
        private String message;
        private HashMap<String, Integer> votes;
        private boolean secret;

        public Poll(Member creator, String heading, List<String> answers, List<String> emotis, Message message, boolean secret) {
            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.emotis = emotis;
            this.votes = new HashMap<>();
            this.message = message.getId();
            this.secret = secret;
        }

        private Member getCreator(Guild guild) {
            return guild.getMember(guild.getJDA().getUserById(creator));
        }

        public Message getMessage(Guild guild) {
            List<Message> messages = new ArrayList<>();
            guild.getTextChannels().forEach(c -> {
                try {
                    messages.add(c.getMessageById(message).complete());
                } catch (Exception e) { }
            });
            return messages.isEmpty() ? null : messages.get(0);
        }

    }

    private static void message(String content) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content);
        channel.sendMessage(eb.build()).queue();
    }

    private static void message(String content, Color color) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content).setColor(color);
        channel.sendMessage(eb.build()).queue();
    }

    private static EmbedBuilder getParsedPoll(Poll poll, Guild guild) {

        StringBuilder ansSTR = new StringBuilder();
        final AtomicInteger count = new AtomicInteger();

        poll.answers.forEach(s -> {
            long votescount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
            ansSTR.append(poll.emotis.get(count.get()) + "  -  " + s + "  -  Votes: `" + votescount + "` \n");
            count.addAndGet(1);
        });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName() + "'s poll.", null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(":pencil:   " + poll.heading + "\n\n" + ansSTR.toString())
                .setColor(Color.cyan);

    }

    private void createPoll(String[] args, MessageReceivedEvent event, boolean secret) {

        if (polls.containsKey(event.getGuild())) {
            message("There is already a vote running on this guild!", Color.red);
            return;
        }

        String argsSTRG = String.join(" ", new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
        List<String> content = Arrays.asList(argsSTRG.split("\\|"));
        String heading = content.get(0);
        List<String> answers = new ArrayList<>(content.subList(1, content.size()));

        Message msg = channel.sendMessage("Creating...").complete();

        List<String> emotis = new ArrayList<>(Arrays.asList(EMOTI));
        List<String> toAddEmotis = new ArrayList<>();
        answers.forEach(a -> {
            int rand = new Random().nextInt(emotis.size() - 1);
            String randEmoti = emotis.get(rand);
            emotis.remove(rand);
            toAddEmotis.add(randEmoti);
        });

        Poll poll = new Poll(event.getMember(), heading, answers, toAddEmotis, msg, secret);

        polls.put(event.getGuild(), poll);

        channel.editMessageById(msg.getId(), getParsedPoll(poll, event.getGuild()).build()).queue();
        toAddEmotis.forEach(s -> msg.addReaction(s).queue());
        channel.pinMessageById(msg.getId()).queue();

        tempList.put(event.getGuild(), polls.get(event.getGuild()).getMessage(event.getGuild()));

        try {
            savePoll(event.getGuild());
        } catch (IOException e) {
            e.printStackTrace();
        }

        event.getMessage().delete().queue();

    }

    private static void addVote(Guild guild, Member author, int voteIndex) {
        Poll poll = polls.get(guild);

        if (poll.votes.containsKey(author.getUser().getId())) {
            tempList.get(guild).getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("Sorry, " + author.getAsMention() + ", you can only vote once!").build()).queue(m ->
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        m.delete().queue();
                    }
                }, 4000)
            );
            return;
        }

        poll.votes.put(author.getUser().getId(), voteIndex);
        polls.replace(guild, poll);
        tempList.get(guild).editMessage(getParsedPoll(poll, guild).build()).queue();


        try {
            savePoll(guild);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void handleReaction(MessageReactionAddEvent event) {

        Guild guild = event.getGuild();


        if (polls.containsKey(guild)) {
            Message msg = null;
            try {
                msg = tempList.get(guild);
            } catch (Exception e) { e.printStackTrace(); }


            if (event.getMessageId().equals(msg != null ? msg.getId() : "") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
                List<String> reactions = msg.getReactions().stream().map(r -> r.getEmote().getName()).collect(Collectors.toList());
                if (reactions.contains(event.getReaction().getEmote().getName())) {
                    addVote(guild, event.getMember(), reactions.indexOf(event.getReaction().getEmote().getName()) + 1);
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }

        }
    }

    private static void savePoll(Guild guild) throws IOException {

        if (!polls.containsKey(guild))
            return;

        File path = new File("SERVER_SETTINGS/" + guild.getId());
        if (!path.exists()) {
            path.mkdirs();
        }

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/betavote.dat";
        Poll poll = polls.get(guild);

        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(poll);
        oos.close();
    }

    private static Poll getPoll(Guild guild) throws IOException, ClassNotFoundException {

        if (polls.containsKey(guild))
            return null;

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/betavote.dat";

        FileInputStream fis = new FileInputStream(saveFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Poll out = (Poll) ois.readObject();
        ois.close();
        return out;
    }

    public static void loadPolls(JDA jda) {

        jda.getGuilds().forEach(g -> {

            File f = new File("SERVER_SETTINGS/" + g.getId() + "/betavote.dat");
            if (f.exists())
                try {
                    Poll poll = getPoll(g);
                    polls.put(g, poll);
                    tempList.put(g, poll.getMessage(g));
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }

        });

    }

    private void statsPoll(MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            message("There is currently no vote running!", Color.red);
            return;
        }

        Poll poll = polls.get(event.getGuild());
        Guild g = event.getGuild();

        if (poll.secret && !poll.getMessage(g).getTextChannel().equals(event.getTextChannel())) {
            message("The running poll is a `secret` poll and can only be accessed from the channel where it was created from!", Color.red);
            return;
        }

        channel.sendMessage(getParsedPoll(poll, g).build()).queue();

    }

    private void closePoll(MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            message("There is currently no vote running!", Color.red);
            return;
        }

        Guild g = event.getGuild();
        Poll poll = polls.get(g);

        if (poll.secret && !poll.getMessage(g).getTextChannel().equals(event.getTextChannel())) {
            message("The running poll is a `secret` poll and can only be accessed from the channel where it was created from!", Color.red);
            return;
        }

        if (!poll.getCreator(g).equals(event.getMember()) && Perms.getLvl(event.getMember()) < 2) {
            message("Only the creator of the poll (" + poll.getCreator(g).getAsMention() + ") can close this poll!", Color.red);
            return;
        }

        tempList.get(event.getGuild()).delete().queue();
        polls.remove(g);
        tempList.remove(g);
        channel.sendMessage(getParsedPoll(poll, g).build()).queue();
        message("Poll closed by " + event.getAuthor().getAsMention() + ".", new Color(0xFF7000));

        new File("SERVER_SETTINGS/" + event.getGuild().getId() + "/betavote.dat").delete();

    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        channel = event.getTextChannel();

        if (args.length < 1) {
            message(help(), Color.red);
            return;
        }

        switch (args[0]) {

            case "create":
                createPoll(args, event, false);
                break;

            case "secret":
                createPoll(args, event, true);
                message("The created vote is `secret` and can only be accessed from this channel.", Color.yellow);
                break;
            case "stats":
                statsPoll(event);
                break;

            case "close":
                closePoll(event);
                break;

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return  "**ATTENTION:** This vote command is not finished yet and may cause heavy bugs and errors!" +
                "USAGE:\n" +
                "**-vote create <question>|<answer 1>|<answer 2>|...**  -  `Create a poll.`\n" +
                "**-vote secret <question>|<answer 1>|<answer 2>|...**  -  `Create a secret poll.\n`" +
                "**-vote stats**  -  `Show the stats of the current vote.`\n" +
                "**-vote close**  -  `Close the current vote.`";
    }

    @Override
    public String description() {
        return "Create a poll. [USAGE OF OWN RISK BETA VERSION]";
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
