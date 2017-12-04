package listeners;

import core.SSSS;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONException;
import utils.STATICS;

import java.io.IOException;
import java.util.ArrayList;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class GuildJoinListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        commands.settings.Botmessage.setSupplyingMessage(event.getJDA());

        if (event.getMember().getUser().isBot()) return;

        if (!SSSS.getSERVERJOINMESSAGE(event.getGuild()).toLowerCase().equals("off")) {
            event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                SSSS.getSERVERJOINMESSAGE(event.getGuild()).replace("[USER]", event.getMember().getAsMention()).replace("[GUILD]", event.getGuild().getName())
            ).queue();
        }

        if (!SSSS.getAUTOROLE(event.getGuild()).equals("")) {

            event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRolesByName(SSSS.getAUTOROLE(event.getGuild()), true)).queue();

            PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
            pc.sendMessage(
                    "**Hey,** " + event.getMember().getAsMention() + " **and welcome on the " + event.getGuild().getName() + " Discord server!**   :wave:\n\n" +
                         "You automatically got assigned the server role `" + SSSS.getAUTOROLE(event.getGuild()) + "` by me.\n\n" +
                         "Now, have a nice day and a lot of fun on the server! ;)"
            ).queue();

            try {
                pc.sendMessage("*And here a nice cat pic. ^^*\n" + commands.chat.Cat.readJsonFromUrl("http://random.cat/meow").get("file").toString()).queue();
            } catch (Exception e) {
                pc.sendMessage("Ooops. I just wanted to attach a little nice cat pic, but the occurred an unexpected error. :(").queue();
            }
        }
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

        commands.settings.Botmessage.setSupplyingMessage(event.getJDA());

        if (event.getMember().getUser().isBot()) return;

        if (!SSSS.getSERVERLEAVEMESSAGE(event.getGuild()).toLowerCase().equals("off")) {
            event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                    SSSS.getSERVERLEAVEMESSAGE(event.getGuild()).replace("[USER]", event.getMember().getAsMention()).replace("[GUILD]", event.getGuild().getName())
            ).queue();
        }

    }


}
