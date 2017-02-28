package listeners;

import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

public class reconnectListener extends ListenerAdapter {

    @Override
    public void onReconnect(ReconnectedEvent event) {

        System.out.println("[INFO] RECONNECT");
        try {

            readyListener.timerOnReady.cancel();
            readyListener.timerOnReady.schedule(readyListener.timerAction, 0, STATICS.refreshTime * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
