package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(
                        "**__GENERAL COMMANDS:__** \n\n" +
                        "` ~ping `  -  Pong!\n" +
                        "` ~cat `  -  =^..^=\n" +
                        "` ~8ball `  -  Ask the holy 8ball for a decision!\n" +
                        "` ~clear `  -  *No function yet*\n" +
                        "` ~bjoke ` alt: ` ~bj `  -  Special fun tool for special guys :^)\n" +
                        "` ~c `  -  Cancels ~bjoke command\n" +
                        "` ~info `  -  Info\n"
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
