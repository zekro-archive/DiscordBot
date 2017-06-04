package listeners;

import commands.etc.CmdLog;
import core.Main;
import core.SSSS;
import core.coreCommands;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

public class botListener extends ListenerAdapter{



    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getChannelType().equals(ChannelType.PRIVATE)) return;

        if (e.getMessage().getContent().startsWith(SSSS.getPREFIX(e.getGuild())) && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId()) {
            try {
                Main.handleCommand(Main.parser.parse(e.getMessage().getContent(), e));
                if (STATICS.commandConsoleOutout)
                    System.out.println(coreCommands.getCurrentSystemTime() + " [Info] [Commands]: Command '" + e.getMessage().getContent() + "' was executed by '" + e.getAuthor() + "' (" + e.getGuild().getName() + ")!");
                ArrayList<String> list = new ArrayList<>();
                list.add(e.getGuild().getId());
                list.add(coreCommands.getCurrentSystemTime());
                list.add(e.getMember().getEffectiveName());
                list.add(e.getMessage().getContent());
                STATICS.cmdLog.add(list);
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}

