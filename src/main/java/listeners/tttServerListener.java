package listeners;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GoldSrcServer;
import net.dv8tion.jda.core.entities.Guild;
import utils.STATICS;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by zekro on 29.03.2017 / 14:24
 * DiscordBot/listeners
 * © zekro 2017
 */

public class tttServerListener {

    static boolean online = false;

    public static void testOnlineState(List<Guild> guilds) {

        GoldSrcServer server;
        try {

            server = new GoldSrcServer(STATICS.TTT_SERVER_IP, STATICS.TTT_SERVER_PORT);
            server.initialize();

            if (!online) {
                for (Guild g : guilds) {
                    g.getPublicChannel().sendMessage(
                            ":regional_indicator_t: :regional_indicator_t: :regional_indicator_t:   **Server now online!** \n\n" +
                                    "Players online: " + server.getPlayers().size() + "\n\n" +
                                    "Use command `  -ttt  ` to get server name and password."
                    ).queue();
                }
                online = true;
            }

        } catch (TimeoutException e) {
            //e.printStackTrace();
            online = false;

        } catch (SteamCondenserException e) {
            //e.printStackTrace();
        }

    }

}
