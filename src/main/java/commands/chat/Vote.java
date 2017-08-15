package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class Vote implements Command {

    /*

        This is the old Vote Command, before the complete rewriting on the new Vote command.
        Because of documentation, how you should NOT write a Vote command, I thought about
        letting this old stuff in, but i'ts nowhere used in this bot.

     */

    private static HashMap<Guild, GVotes> voteHash = new HashMap<>();

    static boolean getHasUserVoted(String username, ArrayList<ArrayList<String>> votesList) {

        for ( ArrayList<String> as : votesList ) {
            if (as.contains(username))
                return true;
        }

        return false;

    }

    void create(String[] args, MessageReceivedEvent event) {

        if (args.length > 1 && !voteHash.containsKey(event.getGuild())) {

            Guild guild = event.getGuild();
            ArrayList<ArrayList<String>> votes = new ArrayList<>();
            User voteCreator = event.getAuthor();
            String argString = "";
            for ( String s : args ) {
                argString += s + " ";
            }
            String[] voteArgs = argString.split("\\|");
            String voteHeading = voteArgs[1];
            ArrayList<String> voteStrings = new ArrayList<>();
            for ( String s : voteArgs ) {
                voteStrings.add(s);
            }

            String voteStringsAsOutput = "";
            int index = 0;
            for ( String s : voteStrings.subList(2, voteArgs.length) ) {
                votes.add(index, new ArrayList<>());
                index++;
                voteStringsAsOutput += "**[" + index + "]** " + s + "\n";
            }

            event.getTextChannel().sendMessage(
                    ":ballot_box_with_check:  **[VOTE] **" + voteHeading + "\n\n" + voteStringsAsOutput
            ).queue();

            voteHash.put(event.getGuild(), new GVotes(guild, votes, voteStrings, voteArgs, voteHeading, voteCreator));

        } else if (args[0].equals("create") && args.length > 0) {
            event.getTextChannel().sendMessage(":warning:  There is already a running poll! Please close that before opening another one.").queue();
            return;
        }

    }

    void vote(String[] args, MessageReceivedEvent event) {

        if (args.length > 1 && voteHash.containsKey(event.getGuild())) {

            int voteIndex = Integer.parseInt(args[1]) - 1;

            ArrayList<ArrayList<String>> votes = voteHash.get(event.getGuild()).votes;
            ArrayList<String> voteStrings = voteHash.get(event.getGuild()).voteStrings;
            String[] voteArgs = voteHash.get(event.getGuild()).voteArgs;
            String voteHeading = voteHash.get(event.getGuild()).voteHeading;

            if (!getHasUserVoted(event.getAuthor().getName(), votes)) {
                votes.get(voteIndex).add(event.getAuthor().getName());
            } else {
                event.getTextChannel().sendMessage(":warning:  Sorry, but you only can vote once!").queue();
                return;
            }


            String voteStringsAsOutput = "";
            int index = 0;
            for ( String s : voteStrings.subList(2, voteArgs.length) ) {
                index++;
                voteStringsAsOutput += "**[" + index + "]** " + s + " - **` " + votes.get(index - 1).size() + " `**\n";
            }

            event.getTextChannel().sendMessage(
                    ":ballot_box_with_check:  **[OPEN VOTE] **" + voteHeading + "\n\n" + voteStringsAsOutput
            ).queue();

        } else if (args[0].equals("vote") && args.length > 1 && !voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(":warning:  Currently there is no poll open! Create one with ` ~vote create |<Heading>|<Answer1>|<Answer2>|... `!").queue();
            return;
        }

    }

    void stats(String[] args, MessageReceivedEvent event) {

        if (args.length > 0 && voteHash.containsKey(event.getGuild())) {

            ArrayList<ArrayList<String>> votes = voteHash.get(event.getGuild()).votes;
            ArrayList<String> voteStrings = voteHash.get(event.getGuild()).voteStrings;
            String[] voteArgs = voteHash.get(event.getGuild()).voteArgs;
            String voteHeading = voteHash.get(event.getGuild()).voteHeading;

            String voteStringsAsOutput = "";
            int index = 0;
            for ( String s : voteStrings.subList(2, voteArgs.length) ) {
                index++;
                voteStringsAsOutput += "**[" + index + "]** " + s + " - **` " + votes.get(index - 1).size() + " `**\n";
            }

            event.getTextChannel().sendMessage(
                    ":ballot_box_with_check:  **[OPEN VOTE] **" + voteHeading + "\n\n" + voteStringsAsOutput
            ).queue();

        } else if (args[0].equals("stats") && args.length > 1 && !voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(":warning:  Currently there is no poll open! Create one with ` ~vote create |<Heading>|<Answer1>|<Answer2>|... `!").queue();
            return;
        }

    }

    void close(String[] args, MessageReceivedEvent event) {

        if (voteHash.containsKey(event.getGuild())) {

            ArrayList<ArrayList<String>> votes = voteHash.get(event.getGuild()).votes;
            ArrayList<String> voteStrings = voteHash.get(event.getGuild()).voteStrings;
            String[] voteArgs = voteHash.get(event.getGuild()).voteArgs;
            String voteHeading = voteHash.get(event.getGuild()).voteHeading;
            User voteCreator = voteHash.get(event.getGuild()).voteCreator;

            if (    voteCreator != event.getAuthor()
                    && !event.getMember().getRoles().contains(event.getGuild().getRolesByName("Owner", true).get(0))
                    && !event.getMember().getRoles().contains(event.getGuild().getRolesByName("Admin", true).get(0))
                    && !event.getMember().getRoles().contains(event.getGuild().getRolesByName("Moderator", true).get(0))
                    ) {
                event.getTextChannel().sendMessage(":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you are not allowed to close a vote you did not created!").queue();
                return;
            }

            String voteStringsAsOutput = "";
            int index = 0;
            for ( String s : voteStrings.subList(2, voteArgs.length) ) {
                index++;
                voteStringsAsOutput += "**[" + index + "]** " + s + " - **` " + votes.get(index - 1).size() + " `**\n";
            }

            event.getTextChannel().sendMessage(
                    ":stop_button:  **[VOTE CLOSED] **" + voteHeading + "\n\n" + voteStringsAsOutput
            ).queue();

            voteHash.remove(event.getGuild());

        } else if (args[0].equals("close") && args.length > 1 && !voteHash.containsKey(event.getGuild())) {
            event.getTextChannel().sendMessage(":warning:  Currently there is no poll open to end! Create one with ` ~vote create |<Heading>|<Answer1>|<Answer2>|... `!").queue();
            return;
        }

    }

    public class GVotes {

        private ArrayList<ArrayList<String>> votes;
        private ArrayList<String> voteStrings;
        private String[] voteArgs;
        private String voteHeading;
        private User voteCreator;

        public GVotes(Guild guild, ArrayList<ArrayList<String>> votes, ArrayList<String> voteStrings, String[] voteArgs, String voteHeading, User voteCreator) {

            this.votes = votes;
            this.voteStrings = voteStrings;
            this.voteArgs = voteArgs;
            this.voteHeading = voteHeading;
            this.voteCreator = voteCreator;

        }

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException {

        switch (args.length > 0 ? args[0] : "") {

            case "create":
                create(args, event);
                break;

            case "vote":
                vote(args, event);
                break;

            case "stats":
                stats(args, event);
                break;

            case "close":
                close(args, event);
                break;

            case "":
                event.getTextChannel().sendMessage(help()).queue();
                break;

        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return
                "USAGE: \n" +
                "`  -vote create |<Title>|<Poss1>|<Poss2>|...  `  -  create a vote\n" +
                "`  -vote vote <index of Poss>  `  -  vote for a possibility\n" +
                "`  -vote stats  `  -  get stats of a current vote\n" +
                "`  -vote close  `  -  close a current vote"
                ;
    }

    @Override
    public String description() {
        return "Create polls";
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
