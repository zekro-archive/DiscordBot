package commands.chat;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zekro on 24.05.2017 / 09:46
 * DiscordBot/commands.chat
 * © zekro 2017
 */


public class Vote2 implements Command  {

    public static HashMap<Guild, Poll> voteHash = new HashMap<>();

    private String[] emoti = {":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:", ":keycap_ten:"};

    public class Poll {

        public Guild guild;
        public Message message;
        public Member creator;
        public List<String> content;
        public HashMap<Member, Integer> votes;

        public Poll(Guild guild, Member creator, Message message, List<String> content) {
            this.message = message;
            this.guild = guild;
            this.creator = creator;
            this.content = content;
            this.votes = new HashMap<>();
        }

    }

    private void createPoll(String[] args, MessageReceivedEvent event) {

        if (voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("A poll is currently running! Please close that poll before opening another one!").build()).queue();
            return;
        }

        StringBuilder argsAS = new StringBuilder();
        Arrays.stream(args).skip(1).forEach(s -> argsAS.append(s + " "));

        List<String> thiscontent = Arrays.asList(argsAS.toString().split("\\|"));

        StringBuilder sb = new StringBuilder();
        int count = 0;
        ArrayList<String> thisconv = new ArrayList<>();
        thiscontent.stream().skip(1).forEach(s -> thisconv.add(s));
        for ( String s : thisconv ) {
            sb.append("" + emoti[count] + "  -  " + s + "\n");
            count++;
        }

        Message msg =   event.getTextChannel().sendMessage(new EmbedBuilder().setColor(new Color(0x007FFF))
                        .setTitle(":pencil:   " + event.getMember().getEffectiveName() + " created new vote.", null)
                        .setDescription("\n" + thiscontent.get(0) + "\n\n" + sb.toString() + "\n\n*Use `" + SSSS.getPREFIX(event.getGuild()) + "vote v <number>` to vote.*")
                        .build()).complete();

        voteHash.put(event.getGuild(), new Poll(event.getGuild(), event.getMember(), msg, thiscontent));

    }

    private void votePoll(String[] args, MessageReceivedEvent event) {

        if (args.length < 2) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
            return;
        } else if (!voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("There is currently no poll running to vote for!").build()).queue();
            return;
        } else if (voteHash.get(event.getGuild()).votes.containsKey(event.getMember())) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("Sorry, " + event.getAuthor().getAsMention() + ", you can only vote **once** for a poll!").build()).queue();
            return;
        }

        int votenumb;
        try {
            votenumb = Integer.parseInt(args[1]);
        } catch (Exception e) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("Please enter a valid number as vote.").build()).queue();
            return;
        }

        if (votenumb > voteHash.get(event.getGuild()).content.stream().skip(1).count()) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("Please enter a valid number as vote.").build()).queue();
            return;
        }

        voteHash.get(event.getGuild()).votes.put(event.getMember(), votenumb);

        event.getMessage().delete().queue();

    }

    private void statsPoll(MessageReceivedEvent event) {

        if (!voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("There is currently no poll running!").build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        ArrayList<String> thisconv = new ArrayList<>();
        voteHash.get(event.getGuild()).content.stream().skip(1).forEach(s -> thisconv.add(s));
        for ( String s : thisconv ) {
            int thiscount = count;
            sb.append("" + emoti[count] + "  -  " + s + "  -  Votes: `" + voteHash.get(event.getGuild()).votes.entrySet().stream().filter(memberIntegerEntry -> memberIntegerEntry.getValue().equals(thiscount + 1)).count() + "` " + "\n");
            count++;
        }

        Message msg =   event.getTextChannel().sendMessage(new EmbedBuilder().setColor(new Color(0x007FFF))
                .setTitle(":pencil:   Current vote from " + event.getMember().getEffectiveName() + ".", null)
                .setDescription("\n" + voteHash.get(event.getGuild()).content.get(0) + "\n\n" + sb.toString())
                .build()).complete();
    }

    private void closePoll(MessageReceivedEvent event) {

        if (!voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("There is currently no poll running to vote for!").build()).queue();
            return;
        }

        if (core.Perms.getLvl(event.getMember()) >= 1 || event.getMember().equals(voteHash.get(event.getGuild()).creator)) {
            statsPoll(event);
            voteHash.remove(event.getGuild());
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(new Color(0xFF5600)).setDescription("Vote closed by " + event.getMember().getAsMention() + ".").build()).queue();
        } else {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("Sorry, " + event.getMember().getAsMention() + ", only the creator of the poll or a member with at least permission level 1 can close a running poll.").build()).queue();
        }

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
        }

        switch (args[0]) {

            case "create":
            case "start":
                createPoll(args, event);
                break;

            case "v":
            case "vote":
                votePoll(args, event);
                break;

            case "votes":
            case "stats":
                statsPoll(event);
                break;

            case "stop":
            case "end":
            case "close":
                closePoll(event);
                break;

            default:
                event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {

        return  "USAGE:\n" +
                "**-vote create <question>|<answer 1>|<answer 2>|...**  -  `Create a poll.`\n" +
                "**-vote v <number>**  -  `Vote for a answer.`\n" +
                "**-vote stats**  -  `Show the stats of the current vote.`\n" +
                "**-vote close**  -  `Close the current vote.`";
    }

    @Override
    public String description() {
        return "Create a poll.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
    }
}
