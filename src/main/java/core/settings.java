package core;

import com.sun.scenario.effect.impl.prism.ps.PPSBlend_REDPeer;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import utils.STATICS;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class settings {

    private static String xmlFile = "settings.xml";
    private static String[] defRoles = {""};

    private static String PREFIX                       = readXmlNodes().get("PREFIX");
    private static String WA_CHANNEL                   = readXmlNodes().get("WA_CHANNEL");
    private static String DOCID_WARFRAMEALERTSFILTER   = readXmlNodes().get("DOCID_WARFRAMEALERTSFILTER");
    private static String DOCID_JOKES                  = readXmlNodes().get("DOCID_JOKES");
    private static String VOICELOG_CHANNEL             = readXmlNodes().get("VOICELOG_CHANNEL");
    private static String COMMAND_CONSOLE_OUTPUT       = readXmlNodes().get("COMMAND_CONSOLE_OUTPUT");


    private static Game GAME                           = new Game() {

        @Override
        public String getName() {
            return readXmlNodes().get("GAME");
        }

        @Override
        public String getUrl() {
            return null;
        }

        @Override
        public GameType getType() {
            return GameType.DEFAULT;
        }
    };
    private static int WA_REFRESHTIME                  = Integer.parseInt(readXmlNodes().get("WA_REFRESHTIME"));
    private static OnlineStatus ONLINE_STATUS          = parseOnlineStatus(readXmlNodes().get("ONLINE_STATUS"));
    private static String[] PERMISSION_ROLES           = readXmlNodes().get("PERMISSION_ROLES").split(",").length > 0 ? readXmlNodes().get("PERMISSION_ROLES").split(",") : defRoles;


    public static OnlineStatus parseOnlineStatus(String in) {

        switch (in) {

            case "online":
                return OnlineStatus.ONLINE;

            case "idle":
                return OnlineStatus.IDLE;

            case "do not distrub":
                return OnlineStatus.DO_NOT_DISTURB;

        }

        return OnlineStatus.ONLINE;
    }

    public static HashMap<String, String> readXmlNodes() {

        HashMap<String, String> out = new HashMap<>();

        try {

            File fXmlFile = new File(xmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("settings").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                out.put(nList.item(i).getNodeName(), nList.item(i).getTextContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public static void initializeSettings() {

        STATICS.PREFIX = PREFIX;

    }

}
