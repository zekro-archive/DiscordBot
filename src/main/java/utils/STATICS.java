package utils;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class STATICS {


    //######### GENERAL BOT SETTINGS #########//

    public static String VERSION = "v1.8.0.0";

    public static String PREFIX = "~";

    public static OnlineStatus STATUS = OnlineStatus.ONLINE;

    public static Game GAME = new Game()  {
        public String getName() {
            return "with boobs. | zekro.de";
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



    //######### OTHER SETTINGS #########//

    public static String voiceLogChannel = "voicelog";

    public static boolean commandConsoleOutout = true;




}
