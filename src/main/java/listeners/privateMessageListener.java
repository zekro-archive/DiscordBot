package listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class PrivateMessageListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        if (event.getMessage().getContent().startsWith("token_"))
            return;

        if (event.getMessage().getContent().equalsIgnoreCase("-disable")) {

            try {
                new File("SERVER_SETTINGS/no_update_info").createNewFile();
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("You disabled update notifications.\n" +
                                        "Now, you wont get automatically notified if there are new versions of the bot available.")
                        .setFooter("Re-enable this function with enetring '-enable'.", null)
                        .build()).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (event.getMessage().getContent().equalsIgnoreCase("-enable")) {


            File f = new File("SERVER_SETTINGS/no_update_info");
            if (f.exists())
                f.delete();

            event.getChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.green)
                    .setDescription("You re-enabled update notification.")
                    .setFooter("Disable this function with enetring '-disable'.", null)
                    .build()).queue();

            return;
        }


        String[] answers = {
                "Hey, " + event.getAuthor().getName() + "! What's going on?",
                "That's nice!",
                "Good job!",
                "I love it so much to eat cookies all day long...",
                "I'm bored.",
                "You are so smart, " + event.getAuthor().getName() + "!",
                "You smell really good, you know? ^^",
                "Tell me more, Senpai.",
                "The weather here is quite nice.",
                "lol",
                "lel",
                "xD",
                ":^)",
                "Did you had a nice day? ^^",
                "I'm talking shit lul",
                "Please get me out of that box... o.o",
                "My real name is Thomas, but please don't tell it someone else... :)"
        };

        try {

            if (!event.getAuthor().equals(event.getJDA().getSelfUser())) {

                Random rand = new Random();
                PrivateChannel pc = event.getAuthor().openPrivateChannel().complete();
                if (event.getMessage().getContent().toLowerCase().contains("hey") || event.getMessage().getContent().toLowerCase().contains("hello")) {
                    pc.sendTyping().queue();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            pc.sendMessage("Hey, " + event.getAuthor().getName() + "! What's going on?").queue();
                        }
                    }, 1000);
                } else {
                    pc.sendTyping().queue();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            pc.sendMessage(answers[rand.nextInt(answers.length)]).queue();
                        }
                    }, 1000);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
