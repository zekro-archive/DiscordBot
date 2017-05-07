package commands.etc;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GameServer;
import com.github.koraktor.steamcondenser.steam.servers.GoldSrcServer;
import com.github.koraktor.steamcondenser.steam.servers.MasterServer;
import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zekro on 29.03.2017 / 12:59
 * DiscordBot/commands.etc
 * © zekro 2017
 */
public class tttServerStatus implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        GoldSrcServer server;
        try {

            server = new GoldSrcServer(STATICS.TTT_SERVER_IP, STATICS.TTT_SERVER_PORT);
            server.initialize();
            event.getTextChannel().sendMessage(
                    "Server online! \n\nPing: " + server.getPing() + " - " + "Players online: " + server.getPlayers().size()
            ).queue();

        } catch (TimeoutException e) {
            //e.printStackTrace();
            event.getTextChannel().sendMessage(
                    "TTT Server is currently offline."
            ).queue();
        } catch (SteamCondenserException e) {
            //e.printStackTrace();
            event.getTextChannel().sendMessage(
                    "TTT Server is currently offline."
            ).queue();
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
        return null;
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }
}
