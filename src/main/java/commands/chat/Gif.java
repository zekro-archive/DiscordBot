package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.brunocvcunha.jiphy.Jiphy;
import org.brunocvcunha.jiphy.JiphyConstants;
import org.brunocvcunha.jiphy.requests.JiphySearchRequest;
import utils.MSGS;
import utils.STATICS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * Created by zekro on 15.09.2017 / 17:09
 * DiscordBot.commands.chat
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Gif implements Command {




    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        TextChannel tc = event.getTextChannel();
        User author = event.getAuthor();

        if (args.length < 1) {
            tc.sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        String query = Arrays.stream(args).filter(s -> !s.startsWith("-")).collect(Collectors.joining("-"));
        int index = args[args.length - 1].startsWith("-") ? Integer.parseInt(args[args.length - 1].substring(1)) - 1 : 0;

        Message msg = event.getTextChannel().sendMessage("Collecting data...").complete();
        event.getMessage().delete().queue();

        Jiphy jiphy = Jiphy.builder()
                .apiKey(JiphyConstants.API_KEY_BETA)
                .build();

        ArrayList<String> gifs = new ArrayList<>();
        jiphy.sendRequest(new JiphySearchRequest(query)).getData().forEach(g ->
                gifs.add(g.getUrl())
        );

        if (gifs.size() == 0) {
            msg.delete().queue();
            tc.sendMessage(MSGS.error().setDescription("No gifs found with the search query `" + query + "`!").build()).queue(m ->
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        m.delete().queue();
                    }
                }, 4000)
            );
            return;
        }
        else if (gifs.size() < index)
            index = gifs.size() - 1;


        msg.editMessage(
                String.format("[%s]\n", author.getName()) +
                gifs.get(index)
        ).queue();


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:\n**" +
               "`-gif <search query> (<-index>)`\n" +
               "*The `-` in front of the optional index numer ist really essential!*";
    }

    @Override
    public String description() {
        return "Send a gif in the chat!";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.chatutils;
    }

    @Override
    public int permission() {
        return 0;
    }
}
