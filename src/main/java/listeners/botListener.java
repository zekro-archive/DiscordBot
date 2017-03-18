package listeners;

import core.Main;
import core.coreCommands;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

public class botListener extends ListenerAdapter{

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getMessage().getContent().startsWith("~") && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId()) {
            e.getTextChannel().sendMessage(
                    ":warning:  Willst du, " + e.getAuthor().getAsMention() + ", dass ich dir antworte? Dann nutze bitte den neuen Präfix **` - `** (\"Minus\"/\"Strich\")!"
            ).queue();
        }

        if (e.getMessage().getContent().startsWith(STATICS.PREFIX) && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId()) {
            try {
                Main.handleCommand(Main.parser.parse(e.getMessage().getContent(), e));
                if (STATICS.commandConsoleOutout)
                    System.out.println(coreCommands.getCurrentSystemTime() + " [Info] [Commands]: Command '" + e.getMessage().getContent() + "' was executed by '" + e.getAuthor() + "' (" + e.getGuild().getName() + ")!");
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onReady(ReadyEvent e) {
        //core.Main.log("STATUS ", "LOGGED IN AS: " + e.getJDA().getSelfUser().getName());
    }

}
