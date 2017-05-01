package core;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class coreCommands {

    public static String getCurrentSystemTime() {
        DateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss]");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
