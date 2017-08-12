package listeners;

import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GildEvents extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        System.out.println(event.getResponseNumber());

    }

}

