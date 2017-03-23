package listeners;

import core.update;
import core.warframeAlertsCore;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.util.Timer;
import java.util.TimerTask;

public class readyListener extends ListenerAdapter {

    public static Timer timerOnReady;
    public static TimerTask timerAction;
    public static ReadyEvent readyEvent;

    public static void restartWarframeAlertsCore() {
        timerOnReady.cancel();
        timerAction = new TimerTask() {
            @Override
            public void run() {

                warframeAlertsCore.pasteAlertsInChat(readyEvent);
                update.getUpdate(readyEvent.getJDA().getGuilds());


            }
        };
        timerOnReady = new Timer();
        timerOnReady.schedule(timerAction, 0, STATICS.refreshTime * 1000);
    }


    @Override
    public void onReady(ReadyEvent event) {

        readyEvent = event;

        if (!STATICS.enableWarframeAlerts && !System.getProperty("os.name").contains("Windows")) {
            System.out.println("[INFO] System: " + System.getProperty("os.name") + " detected - enabled warframe alerts.");
            STATICS.enableWarframeAlerts = true;
        }

        timerOnReady = new Timer();

        timerAction = new TimerTask() {
            @Override
            public void run() {

                warframeAlertsCore.pasteAlertsInChat(event);
                update.getUpdate(readyEvent.getJDA().getGuilds());

            }
        };

        timerOnReady.schedule(timerAction, 0, STATICS.refreshTime * 1000);

    }
}
