package listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class guildJoinListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                ":heart: Willkommen auf unserem Discord-Server , " + event.getMember().getAsMention() + "! :heart:"
        ).queue();

    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                "Tschüss, " + event.getMember().getAsMention() + "...  :cry: "
        ).queue();

    }


}
