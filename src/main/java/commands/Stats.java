package commands;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.ParseException;

/**
 * © zekro 2017
 *
 * @author zekro
 */
public class Stats implements Command {

    static int getMembersOnline(MessageReceivedEvent event, String... role) {

        int count = 0;

        if (role.length > 0) {
            for ( Member m : event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName(role[0], true).get(0)) ) {
                if (!m.getOnlineStatus().equals(OnlineStatus.OFFLINE))
                    count ++;
            }
        } else {
            for ( Member m : event.getGuild().getMembers() ) {
                if (!m.getOnlineStatus().equals(OnlineStatus.OFFLINE))
                    count ++;
            }
        }

        return count;
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException {



        event.getTextChannel().sendMessage(
                ":loudspeaker:  **SERVER STATS**  of Voice Server:  " + event.getGuild().getName() + "\n\n" +
                "Server ID:    " + event.getGuild().getId() + "\n" +
                "Server Owner:   " + event.getGuild().getOwner().getEffectiveName() + " (" + event.getGuild().getOwner().getOnlineStatus() + ") \n" +
                "Members:    " + (event.getGuild().getMembers().size() - event.getGuild().getMembersWithRoles(event.getGuild().getRolesByName("Aushilfen", true).get(0)).size()) + "\n" +
                "Currently Online:    " + (getMembersOnline(event) - getMembersOnline(event, "Aushilfen")) + "\n" +
                "   - Admins:   " + getMembersOnline(event, "Admin") + "\n" +
                "   - Mods:   " + getMembersOnline(event, "Moderator") + "\n" +
                "   - Member:   " + getMembersOnline(event, "Member") + "\n" +
                "   - Bots:   " + getMembersOnline(event, "Aushilfen") + "\n"


        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
