package listeners;

import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;


public class ReconnectListener extends ListenerAdapter {

    @Override
    public void onReconnect(ReconnectedEvent event) {

        System.out.println("[INFO] RECONNECT");

        STATICS.reconnectCount++;

    }

}
