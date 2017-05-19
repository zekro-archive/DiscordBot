package listeners;

import core.SSSS;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONException;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class guildJoinListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        if (event.getMember().getUser().isBot()) return;

        if (!SSSS.getSERVERJOINMESSAGE(event.getGuild()).toLowerCase().equals("off")) {
            event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                SSSS.getSERVERJOINMESSAGE(event.getGuild()).replace("[USER]", event.getMember().getAsMention()).replace("[GUILD]", event.getGuild().getName())
            ).queue();
        }

        List<Role> autoRole = event.getGuild().getRolesByName(SSSS.getAUTOROLE(event.getGuild()), true);

        if (autoRole.size() > 0) {

            PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();

            event.getGuild().getController().addRolesToMember(event.getMember(), autoRole).queue();
            pc.sendMessage(
                    "Hey, " + event.getMember().getAsMention() + "! Welcome on the Server \"" + event.getGuild().getName() + "\"!\n\n" +
                         "You got automatically assigned the role `" + autoRole.get(0).getName() + "`.\n\n" +
                         "So, have a nice day and a lot of fun on the server! :)\n\n" +
                         "*And here to cheer a nice cat picture :^)*"
            ).queue();

            try {
                pc.sendMessage(commands.chat.Cat.readJsonFromUrl("http://random.cat/meow").get("file").toString()).queue();
            } catch (Exception e) {
                pc.sendMessage(MSGS.error.setDescription("Ooops. There was an error sending the cat picture... :(").build()).queue();
            }

        }
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

        if (event.getMember().getUser().isBot()) return;

        if (!SSSS.getSERVERLEAVEMESSAGE(event.getGuild()).toLowerCase().equals("off")) {
            event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                    SSSS.getSERVERLEAVEMESSAGE(event.getGuild()).replace("[USER]", event.getMember().getAsMention()).replace("[GUILD]", event.getGuild().getName())
            ).queue();
        }

    }


}
