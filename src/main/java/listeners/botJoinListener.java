package listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

/**
 * Created by zekro on 04.07.2017 / 15:39
 * DiscordBot.listeners
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class botJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        if (event.getJDA().getGuilds().size() >= 25) {
            event.getGuild().getOwner().getUser().openPrivateChannel().queue(chan -> chan.sendMessage(
                    new EmbedBuilder().setColor(Color.red)
                            .setDescription(
                                    "Sorry, but the maximum server capacity of this public bot is currently set to 25 servers, because of low server performance of the private host the bot is currently hosted on!\n\n" +
                                            "If you want to make it possible to extend the server capacity by hosting the bot on a rented vServer, you can donate to my creator (`zekro#9131`) on his **[patreon](https://www.patreon.com/zekro)** page.\n\n" +
                                            "*The bot was removed automatically from your server by self-kicking.*")
                            .build()
            ).queue());

            event.getGuild().leave().queue();
        }
    }

}
