package listeners;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

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

        if (!STATICS.guildJoinRole.equals(""))
            if (event.getGuild().getRolesByName(STATICS.guildJoinRole, true).size() > 0) {
                event.getGuild().getController().addRolesToMember(
                        event.getMember(), event.getGuild().getRolesByName(STATICS.guildJoinRole, true)
                ).queue();
            }

            PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
            pc.sendMessage("Hey, " + event.getMember().getUser().getAsMention() + "! Willkommen auf dem '" + event.getGuild().getName() + "' - Discord Server!\n\n" +
                    "Dir wurde automatisch die Rolle `  " + event.getGuild().getRolesByName(STATICS.guildJoinRole, true).get(0).getName() + "  ` zugeteilt!\n\n" +
                    "Viel Spaß noch auf dem Server! :*"
            ).queue();
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                "Tschüss, " + event.getMember().getAsMention() + "...  :cry: "
        ).queue();

    }


}
