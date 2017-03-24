package commands.essentials;

import commands.Command;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * © zekro 2017
 *
 * @author zekro
 */
public class UserInfo implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss (z)");

        Member memb;

        if (args.length > 0) {
            memb = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
        } else {
            memb = event.getMember();
        }

        String NAME = memb.getEffectiveName();
        String GUILD_JOIN_DATE = memb.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String DISCORD_JOINED_DATE = memb.getUser().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String ID = memb.getUser().getId();
        String DISCRIMINATOR = memb.getUser().getDiscriminator();
        String STATUS = memb.getOnlineStatus().getKey();
        String ROLES = "";
        String GAME = "";
        String AVATAR = memb.getUser().getAvatarUrl();

        try {
            GAME = memb.getGame().getName();
        } catch (Exception e) {
            GAME = "-/-";
        }

        for ( Role r : memb.getRoles() ) {
            ROLES += r.getName() + ", ";
        }
        if (ROLES.length() > 0)
            ROLES = ROLES.substring(0, ROLES.length()-2);
        else
            ROLES = "No roles on this server.";

        event.getTextChannel().sendMessage(
                ":spy:   **User information for " + memb.getUser().getName() + ":**\n\n" +
                "Name:   **" + NAME + "**\n" +
                "User:   **" + NAME + "#" + DISCRIMINATOR + "**\n" +
                "ID:   **" + ID + "**\n" +
                "Curr. Status:   **" + STATUS + "**\n" +
                "Curr. Game:   **" + GAME + "**\n" +
                "Roles:   **" + ROLES + "**\n" +
                "Guild Joined:   **" + GUILD_JOIN_DATE + "**\n" +
                "Discord Joined:   **" + DISCORD_JOINED_DATE + "**\n" +
                "Avatar-URL:   *" + AVATAR + "*\n"
        ).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Get some info about a user";
    }

}
