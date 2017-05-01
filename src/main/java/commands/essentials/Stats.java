package commands.essentials;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * © zekro 2017
 *
 * @author zekro
 */
public class Stats implements Command {

    private long getOnlineMembers(Guild guild) {
        return guild.getMembers().stream()
                .filter(member -> !member.getOnlineStatus().equals(OnlineStatus.OFFLINE))
                .filter(member -> !member.getUser().isBot())
                .count();
    }

    private long getMembers(Guild guild) {
        return guild.getMembers().stream().filter(member -> !member.getUser().isBot()).count();
    }

    private HashMap<Role, Map.Entry<Integer, Integer>> getRoleMembersCount(Guild guild) {

        HashMap<Role, Map.Entry<Integer, Integer>> out = new HashMap<>();

        for (Role role : guild.getRoles()) {
            int count = 0;
            int countOnline = 0;
            for (Member member : guild.getMembers()) {
                if (member.getRoles().contains(role)) {
                    count++;
                    if (!member.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                        countOnline++;
                    }
                }
            }
            out.put(role, new AbstractMap.SimpleEntry<>(count, countOnline));
        }

        return out;
    }

    private String getRoleInfos(HashMap<Role, Map.Entry<Integer, Integer>> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((role, integerIntegerEntry) -> {
            if (map.get(role).getKey() != 0)
                sb.append("      - **" + role.getName() + ":**    " + map.get(role).getKey() + "  (Online: " + map.get(role).getValue() + ")" + "\n");
        });
        return sb.toString();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException {

        Guild guild = event.getGuild();
        event.getTextChannel().sendMessage(
                "*" + guild.getName() + "*  -  **SERVER STATS**\n\n" +
                        " - **Server Name:**    " + guild.getName() + "\n" +
                        " - **Server ID:**    " + guild.getId() + "\n" +
                        " - **Server Owner:**    " + guild.getOwner().getAsMention() + " (" + guild.getOwner().getOnlineStatus().toString() + ") " + "\n" +
                        " - **Members:**    " + getMembers(guild) + " (Online: " + getOnlineMembers(guild) + ")" + "\n" +
                        " - **Roles:** " + "\n" + getRoleInfos(getRoleMembersCount(guild))
        ).queue();

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
        return "Get stats of the server";
    }
}
