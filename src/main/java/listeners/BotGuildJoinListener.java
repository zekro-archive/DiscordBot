package listeners;

import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zekro on 27.09.2017 / 21:57
 * DiscordBot.listeners
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class BotGuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        Guild guild = event.getGuild();
        GuildController controller = guild.getController();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                controller.createCategory("zekroBot").queue(cat -> {

                    controller.modifyCategoryPositions()
                            .selectPosition(cat.getPosition())
                            .moveTo(0).queue();

                    String[] list = {"music", "commands", "cmdlog", "warframealerts", "voicelog"};

                    Arrays.stream(list).forEach(s ->
                            controller.createTextChannel(s).queue(chan -> chan.getManager().setParent((Category) cat).queue())
                    );
                });

            }
        }, 500);

    }


}
