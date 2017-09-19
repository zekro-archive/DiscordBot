package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;

public class Say implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (core.Perms.check(1, event)) return;

        String msg = String.join(" ", args);
        User author = event.getMessage().getAuthor();

        event.getMessage().delete().queue();

        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.cyan)
                .setAuthor(author.getName(), null, author.getAvatarUrl())
                .setDescription(msg)
                .build()
        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE: -say <message>";
    }

    @Override
    public String description() {
        return "Say something as the bot in chat";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
    }

    @Override
    public int permission() {
        return 1;
    }
}
