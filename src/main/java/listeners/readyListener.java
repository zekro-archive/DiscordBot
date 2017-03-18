package listeners;

import core.warframeAlertsCore;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / listener
 * © zekro 2017
 */


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

            }
        };

        timerOnReady.schedule(timerAction, 0, STATICS.refreshTime * 1000);

        /* Some code for experimenting around with prefix settings
        File f = new File("servers");

        if (!f.exists())
            f.mkdir();

        List<Guild> serverList = event.getJDA().getGuilds();
        for (Guild g : serverList) {

            String path = "servers/" + g.getName().toString();
            String settingsPath = path + "/settings.xml";

            File fg = new File(path);
            if (!fg.exists())
                fg.mkdir();

            fg = new File(settingsPath);
            if (!fg.exists()) {
                try {

                    xmlParser.createXML(settingsPath);

                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            } else {
                try {

                    System.out.println(xmlParser.readXML(settingsPath).get("prefix"));
                    STATICS.PREFIX = xmlParser.readXML(settingsPath).get("prefix");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        */

    }
}
