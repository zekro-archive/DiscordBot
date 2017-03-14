package utils;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class STATICS {


    //######### GENERAL BOT SETTINGS #########//

    public static String VERSION = "v1.10.0.0";

    public static String PREFIX = "~";

    public static OnlineStatus STATUS = OnlineStatus.ONLINE;

    public static Game GAME = new Game()  {
        public String getName() {
            return "with boobs. | dev.zekro.de | v." + VERSION;
        }
        public String getUrl() {
            return "http://zekro.de";
        }
        public GameType getType() {
            return GameType.DEFAULT;
        }
    };



    //######### WARFRAME ALERTS SETTINGS #########//

    public static int refreshTime = 10;

    public static String warframeAlertsChannel = "warframealerts";

    public static String warframeAlertsServerID = "266589518537162762";

    public static boolean enableWarframeAlerts = false;



    //######### PERMISSION SETTINGS #########//

    public static String[] botPermRoles = {"Bot Commander", "Moderator", "Admin", "Owner"};


    //########## GOOGLE DOCS ID'S ##########//

    public static String DOCID_warframeAlertsFilter = "13O2lZ_UemLDkCV8425XHOPSZ3aVoeYmV5cF_vLQAyEY";

    public static String DOCID_jokes = "1fWHPIrZKHSXBsF5SWO3ZEHmecItVppYvM39pm7Rvssk";

    //########### OTHER SETTINGS ###########//

    public static String voiceLogChannel = "voicelog";

    public static boolean commandConsoleOutout = true;




}
