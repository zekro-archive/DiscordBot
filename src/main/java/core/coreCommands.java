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

        return userHasPermission;
    }
}
