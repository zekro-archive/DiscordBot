package listeners;

import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.xmlParser;
import org.xml.sax.SAXException;
import utils.STATICS;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class readyListener extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent event) {


        Timer timer = new Timer();
        TimerTask timerAction = new TimerTask() {
            @Override
            public void run() {

                warframeAlertsCore.pasteAlertsInChat(event);

            }
        };

        timer.schedule(timerAction, 0, 10000);

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
