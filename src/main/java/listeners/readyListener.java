package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.xmlParser;
import org.xml.sax.SAXException;
import utils.STATICS;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class readyListener extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent event) {

        System.out.println(event.getJDA().getGuilds().get(1));
        String alertsServerID = "266589518537162762";
        String alertsChannelName = "warframealerts";


        Timer timer = new Timer();
        TimerTask timerAction = new TimerTask() {
            @Override
            public void run() {
                try {

                    if (warframeAlertsCore.checkForListUpdate() && warframeAlertsCore.getFilteredAlerts(warframeAlertsCore.getFilter(), warframeAlertsCore.getAlerts()).size() != 0) {
                        event.getJDA().getGuildById(alertsServerID).getTextChannelsByName(alertsChannelName, false).get(0).sendMessage(
                                warframeAlertsCore.getAlertsAsMessage(warframeAlertsCore.getFilteredAlerts(warframeAlertsCore.getFilter(), warframeAlertsCore.getAlerts()))
                        ).queue();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };

        timer.schedule(timerAction, 0, 1000);



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
        }

    }
}
