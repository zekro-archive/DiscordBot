package core;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.util.Arrays;

/**
 * Created by zekro on 18.03.2017 / 01:57
 * DiscordBot / core
 * © zekro 2017
 */


public class permsCore {

    public static boolean checkPermission(MessageReceivedEvent event) {
        boolean userHasPermission = false;
        for ( Role r : event.getGuild().getMember(event.getAuthor()).getRoles() ) {
            if (Arrays.stream(STATICS.botPermRoles).parallel().anyMatch(r.getName()::contains))
                userHasPermission = true;
        }

        return userHasPermission;
    }

}
