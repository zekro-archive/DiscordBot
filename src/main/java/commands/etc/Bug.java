package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.http.auth.AUTH;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * Created by zekro on 01.07.2017 / 10:50
 * DiscordBot.commands.etc
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Bug implements Command {

    public static User AUTHOR;
    public static Message MESSAGE;
    public static EmbedBuilder FINAL_MESSAGE;
    public static TextChannel CHANNEL;
    public static Timer TIMER = new Timer();


    public static void sendConfMessage() {
        MESSAGE.getTextChannel().sendMessage(MSGS.success().setDescription("Submit sucesfully send!").build()).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        try {

            CHANNEL = event.getJDA().getGuildById("307084334198816769").getTextChannelById("330636155668135936");

        } catch (Exception e) {

            if (args.length < 1) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setDescription(
                        "If you want to submit a bug or a suggestion, please use the public **[google sheet](https://s.zekro.de/botsubs)**."
                ).build()).queue();
                return;
            }
            event.getTextChannel().sendMessage(MSGS.error().setDescription(
                    "Sorry, the expandet version of this command is only available on the public version of the bot!\n\n" +
                    "If you want to submit a bug or a suggestion, please use the public **[google sheet](https://s.zekro.de/botsubs)**."
            ).build()).queue();
            return;
        }

        if (args.length < 1)
            event.getTextChannel().sendMessage(new EmbedBuilder().setDescription(
                    "You can submit a bug report or suggestion right with the command using like this:\n" +
                    "```-bug <message>```\n\n" +
                    "You can use following modifiers to embed better details:\n" +
                    "```-bug <title>\n" +
                    "<bug/gussestion>\n" +
                    "<message>```\n\n" +
                    "Otherwise you can also just use the public **[google sheet](https://s.zekro.de/botsubs)** to submit a bug or suggestion."
            ).build()).queue();
        else {

            String title = "Bug report / Suggestion";
            String type = "unspecified";
            String message = "";

            String argsString = Arrays.stream(args).collect(Collectors.joining(" "));

            title = argsString.split("\n").length > 3 ? argsString.split("\n")[0] : title;
            type = argsString.split("\n").length > 3 ? argsString.split("\n")[1] : type;
            message = argsString.split("\n").length > 3 ? Arrays.stream(argsString.split("\n")).skip(2).collect(Collectors.joining("\n")) : argsString;

            MESSAGE = event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setTitle("MESSAGE PREVIEW", null)
                    .addField("Title", title, true)
                    .addField("Type", type, true)
                    .addField("Message", message, false)
                    .setFooter("Click on the reaction \uD83D\uDC4D to send the message. Else just dont klick ;)", null)
                    .build()
            ).complete();
            MESSAGE.addReaction("\uD83D\uDC4D").queue();
            AUTHOR = event.getMessage().getAuthor();

            FINAL_MESSAGE = new EmbedBuilder()
                    .addField("Message ID", event.getMessage().getId(), false)
                    .addField("Author", event.getAuthor().getName(), false)
                    .addField("Title", title, true)
                    .addField("Type", type, true)
                    .addField("Message", message, false);

            TIMER = new Timer();
            TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    AUTHOR = null;
                    MESSAGE = null;
                    FINAL_MESSAGE = null;
                    CHANNEL = null;
                    TIMER = null;
                    event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Confirmation time expired.").build()).queue();
                }
            }, 20000);

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
        return "Send a bug report or a suggestion";
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
