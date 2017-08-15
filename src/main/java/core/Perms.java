package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by zekro on 30.04.2017 / 21:10
 * DiscordBot/core
 * © zekro 2017
 */

public class Perms {


    public static boolean isOwner(User user, TextChannel channel) {
        if (STATICS.BOT_OWNER_ID == 0) {
            channel.sendMessage(MSGS.error().setDescription("There is no owner ID set in `SETTINGS.txt`.\nIf you are the owner of this bot, please add your Discord user id in the `SETTINGS.txt`!").build()).queue();
            return false;
        }
        if (user.getId().equals(String.valueOf(STATICS.BOT_OWNER_ID)))
            return true;
        channel.sendMessage(MSGS.error().setDescription("Only the bots owner (" + user.getJDA().getUserById(STATICS.BOT_OWNER_ID).getAsMention() + ") can use this command.").build()).queue();
        return false;

    }

    public static int getLvl(Member member) {

        if (member.equals(member.getGuild().getOwner()))
            return 3;
        if (member.getRoles().stream().anyMatch(role -> Arrays.stream(SSSS.getPERMROLES_2(member.getGuild())).anyMatch(s1 -> role.getName().equals(s1)))) {
            return 2;
        } else if (member.getRoles().stream().anyMatch(role -> Arrays.stream(SSSS.getPERMROLES_1(member.getGuild())).anyMatch(s -> role.getName().equals(s)))) {
            return 1;
        }
        return 0;
    }

    public static boolean check(int required, MessageReceivedEvent event) {
        if (required > getLvl(event.getMember())) {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    "Sorry but you need permission level `" + required + "` or above!\n(Your current permission level is `" + getLvl(event.getMember()) + "`)."
            ).build()).queue();
            return true;
        }
        return false;
    }

    //public static boolean test(MessageReceivedEvent event) {
    //
    //    boolean userNotPermitted = true;
    //    for ( Role r : event.getGuild().getMember(event.getAuthor()).getRoles() ) {
    //        if (Arrays.stream(STATICS.botPermRoles).parallel().anyMatch(r.getName()::contains))
    //            userNotPermitted = false;
    //    }
    //
    //    StringBuilder sb = new StringBuilder();
    //    Arrays.stream(STATICS.botPermRoles).forEach(s -> sb.append(s + ", "));
    //    EmbedBuilder eb = MSGS.error
    //            .addField("Error Type", "Missing permissions.", false)
    //            .addField("Description", "You need at least one of the following roles to access that command:\n" + sb.toString().substring(0, sb.toString().length() - 2), false);
    //
    //    if (userNotPermitted) event.getTextChannel().sendMessage(eb.build()).queue();
    //
    //    return userNotPermitted;
    //}

}
