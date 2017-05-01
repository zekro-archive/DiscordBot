package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
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
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(":warning:  Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permission to use this command! \nOne of these roles required: " + sb.toString().substring(0, sb.length() - 1));

        if (userNotPermitted) event.getTextChannel().sendMessage(eb.build()).queue();

        return userNotPermitted;
    }

}
