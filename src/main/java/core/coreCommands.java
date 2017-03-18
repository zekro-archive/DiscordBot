package core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / core
 * © zekro 2017
 */

public class coreCommands {

    public static String getCurrentSystemTime() {
        DateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss]");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
