package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class Vote implements Command {

    static boolean voteActive = false;
    static ArrayList<ArrayList<String>> votes = new ArrayList<>();
    static ArrayList<String> voteStrings;
    static String[] voteArgs;
    static String voteHeading;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException {

        if (args.length > 0) {

            /**
             * Create a poll.
             */
            if (args[0].equals("create") && args.length > 1 && !voteActive) {

                votes = new ArrayList<>();

                String argString = "";
                for ( String s : args ) {
                    argString += s + " ";
                }

                voteArgs = argString.split("\\|");
                voteHeading = voteArgs[1];

                voteStrings = new ArrayList<>();
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

                voteActive = true;

            } else if (args[0].equals("create") && args.length > 0) {
                event.getTextChannel().sendMessage(":warning:  There is already a running poll! Please close that before opening another one.").queue();
                return;
            }


            /**
             * Vote for a poll answer.
             */
            if (args[0].equals("vote") && args.length > 1 && voteActive) {

                int voteIndex = Integer.parseInt(args[1]) - 1;

                if (!getHasUserVoted(event.getAuthor().getName(), votes)) {
                    votes.get(voteIndex).add(event.getAuthor().getName());
                } else
                    event.getTextChannel().sendMessage(":warning:  Sorry, but you only can vote once!").queue();



            } else if (args[0].equals("vote") && args.length > 1 && !voteActive) {
                event.getTextChannel().sendMessage(":warning:  Currently there is no poll open! Create one with ` ~vote create |<Heading>|<Answer1>|<Answer2>|... `!").queue();
                return;
            }

            /**
             * Get the current stats of an active poll.
             */
            if (args[0].equals("stats") && args.length > 0 && voteActive) {

                String voteStringsAsOutput = "";
                int index = 0;
                for ( String s : voteStrings.subList(2, voteArgs.length) ) {
                    index++;
                    voteStringsAsOutput += "**[" + index + "]** " + s + " - **` " + votes.get(index - 1).size() + " `**\n";
                }

                event.getTextChannel().sendMessage(
                        ":ballot_box_with_check:  **[OPEN VOTE] **" + voteHeading + "\n\n" + voteStringsAsOutput
                ).queue();

            } else if (args[0].equals("stats") && args.length > 1 && !voteActive) {
                event.getTextChannel().sendMessage(":warning:  Currently there is no poll open! Create one with ` ~vote create |<Heading>|<Answer1>|<Answer2>|... `!").queue();
                return;
            }


            if (args[0].equals("close") && args.length > 0 && voteActive) {

                String voteStringsAsOutput = "";
                int index = 0;
                for ( String s : voteStrings.subList(2, voteArgs.length) ) {
                    index++;
                    voteStringsAsOutput += "**[" + index + "]** " + s + " - **` " + votes.get(index - 1).size() + " `**\n";
                }

                event.getTextChannel().sendMessage(
                        ":stop_button:  **[VOTE CLOSED] **" + voteHeading + "\n\n" + voteStringsAsOutput
                ).queue();

                voteActive = false;
                votes = new ArrayList<>();

            } else if (args[0].equals("close") && args.length > 1 && !voteActive) {
                event.getTextChannel().sendMessage(":warning:  Currently there is no poll open to end! Create one with ` ~vote create |<Heading>|<Answer1>|<Answer2>|... `!").queue();
                return;
            }
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }


    static boolean getHasUserVoted(String username, ArrayList<ArrayList<String>> votesList) {

        for ( ArrayList<String> as : votesList ) {
            if (as.contains(username))
                return true;
        }

        return false;

    }

}
