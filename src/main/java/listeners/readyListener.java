package listeners;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.util.Timer;
import java.util.TimerTask;

public class readyListener extends ListenerAdapter{

    public static Timer timerOnReady = new Timer();
    public static TimerTask timerAction;

    @Override
    public void onReady(ReadyEvent event) {

        if (!STATICS.enableWarframeAlerts && !System.getProperty("os.name").contains("Windows")) {
            System.out.println("[INFO] System: " + System.getProperty("os.name") + " detected - enabled warframe alerts.");
            STATICS.enableWarframeAlerts = true;
        }

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
