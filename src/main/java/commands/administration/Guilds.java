package commands.administration;

import commands.Command;
import core.Perms;
import core.SSSS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;

/**
 * Created by zekro on 11.06.2017 / 11:02
 * DiscordBot/commands.administration
 * © zekro 2017
 */

public class Guilds implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (!Perms.isOwner(event.getAuthor(), event.getTextChannel())) return;

        if ((args.length > 1 ? args[0] : "").equalsIgnoreCase("info")) {
            Guild g;
            try {
                g = event.getJDA().getGuildById(args[1]);
            } catch (Exception e) {
                event.getTextChannel().sendMessage(MSGS.error().setDescription("There is no guild with the entered ID.").build()).queue();
                return;
            }

            event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Guild information for guild " + g.getName(), null)
                    .setThumbnail(g.getIconUrl())
                    .setDescription("\n\n")
                    .addField("Name", g.getName(), false)
                    .addField("ID", g.getId(), false)
                    .addField("Owner", g.getOwner().getAsMention(), false)
                    .addField("Members", g.getMembers().size() + " (Online: " + g.getMembers().stream().filter(m -> !m.getOnlineStatus().equals(OnlineStatus.OFFLINE)).count() + ")", false)
                    .addField("Prefix on server", "`" + SSSS.getPREFIX(g) + "`", false)
                    .build()
            ).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("**Running on `" + event.getJDA().getGuilds().size() + "` guilds.**\n\n");
        event.getJDA().getGuilds().stream()
                .sorted(Comparator.comparingInt(s -> s.getMembers().size()))
                .forEach(g -> sb.append(
                "*[" + g.getMembers().size() + "]*  -  **" + g.getName() + "**  -  `" + g.getId() + "`\n"
        ));

        if (sb.toString().length() > 2000)
            event.getTextChannel().sendMessage("**Guilds:**\n\n" + sb.toString()).queue();
        else
            event.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Guilds", null).setDescription(sb.toString()).build()).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Get list of guilds bot is running on";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.administration;
    }

    @Override
    public int permission() {
        return 3;
    }
}
