package listeners;

import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.util.Timer;

import static listeners.readyListener.restartWarframeAlertsCore;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / listener
 * © zekro 2017
 */


public class reconnectListener extends ListenerAdapter {

    @Override
    public void onReconnect(ReconnectedEvent event) {

        System.out.println("[INFO] RECONNECT");

        restartWarframeAlertsCore();
    }

}
