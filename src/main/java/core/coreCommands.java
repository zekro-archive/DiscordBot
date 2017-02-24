package core;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.util.Arrays;

public class coreCommands {

    public static boolean checkPermission(MessageReceivedEvent event) {
        boolean userHasPermission = false;
        for ( Role r : event.getGuild().getMember(event.getAuthor()).getRoles() ) {
            if (Arrays.stream(STATICS.botPermRoles).parallel().anyMatch(r.getName()::contains))
                userHasPermission = true;
        }

        /* Only for single Strings aa Role
        for ( Role r : event.getGuild().getMember(event.getAuthor()).getRoles() ) {
            if (r.getName().contains(STATICS.defaultRole))
                userHasPermission = true;
        }
        */
        return userHasPermission;
    }
}
