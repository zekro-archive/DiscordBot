package listeners;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class privateMessageListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

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



        //PrivateChannel pc = null;
        //try {
        //    pc = event.getAuthor().openPrivateChannel().complete();
        //    pc.sendMessage(
        //            ":warning:  Hey! Please don't send private messages to the bot's account! Sorry, but nobody will receive them. :crying_cat_face: "
        //    ).queue();
        //} catch (Exception e) {}


    }

}
