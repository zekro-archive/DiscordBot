package listeners;

import core.Main;
import core.SSSS;
import core.coreCommands;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

public class botListener extends ListenerAdapter{

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getChannelType().equals(ChannelType.PRIVATE)) return;

        if (e.getMessage().getContent().startsWith(SSSS.getPREFIX(e.getGuild())) && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId()) {
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

}
