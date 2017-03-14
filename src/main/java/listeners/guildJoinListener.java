package listeners;

import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * © zekro 2017
 *
 * @author zekro
 */
public class guildJoinListener extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                ":heart: Willkommen auf dem Server " + event.getGuild().getName() + ", " + event.getResponseNumber() + " :heart:"
        );

    }

}
