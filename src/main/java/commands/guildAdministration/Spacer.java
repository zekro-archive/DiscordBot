package commands.guildAdministration;

import commands.Command;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zekro on 01.07.2017 / 13:36
 * DiscordBot.commands.guildAdministration
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Spacer implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (core.Perms.check(1, event)) return;

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        switch (args[0]) {

            case "add":
            case "create":

                if (!event.getMember().getVoiceState().inVoiceChannel()) {
                    event.getTextChannel().sendMessage(MSGS.error().setDescription("You need to be in a voice channel to add a spacer.").build()).queue();
                    return;
                }

                Channel vc = event.getGuild().getController().createVoiceChannel("-------------------------").complete();
                event.getGuild().getController().modifyVoiceChannelPositions().selectPosition(vc.getPosition()).moveTo(event.getMember().getVoiceState().getChannel().getPosition() + 1).queue();

                break;

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:\n**Create a spacer under the voice channel you are in with\n`-spacer create` or `-spacer add`";
    }

    @Override
    public String description() {
        return "Create spacer voice channels";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.guildadmin;
    }

    @Override
    public int permission() {
        return 1;
    }
}
