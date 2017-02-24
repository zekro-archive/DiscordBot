package utils;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class STATICS {

    public static String VERSION = "v1.5.2b";

    public static String PREFIX = "~";

    public static Game GAME = new Game() {
        public String getName() {
            return "with boobs.";
        }

        public String getUrl() {
            return "http://zekro.jimdo.com";
        }

        public GameType getType() {
            return GameType.DEFAULT;
        }
    };

    public static String[] botPermRoles = {"Bot Commander", "Moderator", "Admin", "Owner"};

    public static OnlineStatus STATUS = OnlineStatus.ONLINE;

    public static boolean enableWarframeAlerts = true;

}
