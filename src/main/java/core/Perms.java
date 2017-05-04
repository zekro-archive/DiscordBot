package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.util.Arrays;

/**
 * Created by zekro on 30.04.2017 / 21:10
 * DiscordBot/core
 * © zekro 2017
 */

public class Perms {

    public static boolean test(MessageReceivedEvent event) {

        boolean userNotPermitted = true;
        for ( Role r : event.getGuild().getMember(event.getAuthor()).getRoles() ) {
            if (Arrays.stream(STATICS.botPermRoles).parallel().anyMatch(r.getName()::contains))
                userNotPermitted = false;
        }

        StringBuilder sb = new StringBuilder();
        Arrays.stream(STATICS.botPermRoles).forEach(s -> sb.append(s + ", "));
        EmbedBuilder eb = MSGS.error
                .addField("Error Type", "Missing permissions.", false)
                .addField("Description", "You need at least one of the following roles to access that command:\n" + sb.toString().substring(0, sb.toString().length() - 2), false);

        if (userNotPermitted) event.getTextChannel().sendMessage(eb.build()).queue();

        return userNotPermitted;
    }

}
