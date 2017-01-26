package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Info implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(
                "**zekroBot** JDA Discord Bot - v1.0.2b\n\n" +
                "http://zekrodev.jimdo.com\n" +
                "http://github.com/zekrotja/DiscordBot\n\n" +
                "*© 2017 zekro*"
        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
