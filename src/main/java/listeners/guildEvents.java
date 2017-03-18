package listeners;

import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / listener
 * © zekro 2017
 */


public class guildEvents extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        System.out.println(event.getResponseNumber());

    }

}

