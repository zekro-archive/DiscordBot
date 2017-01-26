package commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BJoke implements Command {

    public static final String HELP = "USAGE: ~bjoke @user OR ~bj @user";

    public boolean called(String[] args, MessageReceivedEvent event) {

        return false;
    }

    public int sec = 10;

    public void action(String[] args, final MessageReceivedEvent event) {

        try {
            String a = args[0];
        } catch (Exception e) {
            event.getTextChannel().sendMessage(HELP).queue();
            return;
        }

        GuildController gc = new GuildController(event.getGuild());

        Member victim;

        try {
            victim = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
        } catch (Exception e) {
            event.getTextChannel().sendMessage(HELP).queue();
            return;
        }


        event.getTextChannel().sendMessage(
                event.getMessage().getMentionedUsers().get(0).getAsMention() + " hat einen schlechten Witz gerissen!\n\n" +
                        "Wenn nach 10 Sekunden keiner lacht (wenn doch: '~c') wird er vom Voice Channel gekickt!\n"

        ).queue();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!BJokeCancle.canceled && sec >= 0) {
                    event.getMessage().getTextChannel().sendMessage(sec + "...").queue();
                    sec--;
                } else if (!BJokeCancle.canceled) {
                    event.getMessage().getTextChannel().sendMessage(
                            "Haha, " + event.getMessage().getMentionedUsers().get(0).getAsMention() + ", nieman hat über deinen schlechten Witz gelacht!"
                    ).queue();

                    gc.moveVoiceMember(victim, event.getGuild().getVoiceChannels().get(0)).queue();
                    try {
                        gc.setNickname(victim, victim.getNickname() + " der Unlustige").queue();
                    } catch (PermissionException e) {
                        event.getMessage().getTextChannel().sendMessage("[ERROR] Can't modify a member with higher or equal highest role than the bot!").queue();
                    }
                    timer.cancel();
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);


        BJokeCancle.canceled = false;


    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public String help() {
        return HELP;
    }
}