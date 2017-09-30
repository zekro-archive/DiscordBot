package utils;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class STATICS {

    public static String TOKEN = "";

    //######### GENERAL BOT SETTINGS #########//


    public static String VERSION = "1.42.0.0";


    public static String PREFIX = "-";

    public static OnlineStatus STATUS = OnlineStatus.ONLINE;

    public static String CUSTOM_MESSAGE = "ゼクロ";

    public static Game GAME = Game.of(CUSTOM_MESSAGE + " | -help | v." + VERSION);


    //######### WARFRAME ALERTS SETTINGS #########//

    public static int refreshTime = 10;

    public static String warframeAlertsChannel = "warframealerts";

    public static boolean enableWarframeAlerts = true;



    //######### PERMISSION SETTINGS #########//

    public static String[] PERMS = {"Bot Commander", "Moderator", "Admin", "Owner"};
    public static String[] FULLPERMS = {"Admin", "Owner"};

    public static String guildJoinRole = "";


    //########## GOOGLE DOCS ID'S ##########//

    public static String DOCID_warframeAlertsFilter = "13O2lZ_UemLDkCV8425XHOPSZ3aVoeYmV5cF_vLQAyEY";

    public static String DOCID_jokes = "1fWHPIrZKHSXBsF5SWO3ZEHmecItVppYvM39pm7Rvssk";

    //########### OTHER SETTINGS ###########//

    public static String voiceLogChannel = "voicelog";

    public static String musicChannel = "mucke";

    public static boolean commandConsoleOutout = true;

    public static String KICK_VOICE_CHANNEL = "";


    public static boolean autoUpdate = true;

    public static boolean musicCommandsOnlyInMusicChannel = false;

    public static String input;

    public static int music_volume = 0;

    public static String discordJoinMessage = ":heart: Hey, [USER]! Welcome on the [GUILD]! :heart:";

    public class CMDTYPE {
        public static final String administration = "Administration";
        public static final String chatutils = "Chat Utilities";
        public static final String essentials = "Essentials";
        public static final String etc = "Etc";
        public static final String music = "Music";
        public static final String guildadmin = "Guild Administration";
        public static final String settings = "SettingsCore";
    }

    public static Date lastRestart;

    public static int reconnectCount = 0;

    public static ArrayList<ArrayList<String>> cmdLog = new ArrayList<>();

    public static long BOT_OWNER_ID = 0;

    public static int MUSIC_BUFFER = 1000;

    public static int SERVER_LIMIT = 250;

}
