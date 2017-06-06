package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zekro on 06.06.2017 / 10:52
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class Rand6 implements Command {

    private final String[] attc = {"Blitz", "IQ", "Twitch", "Montagne", "Ashe", "Thermite", "Sledge", "Thatcher", "Capitão", "Jackal", "Hibana", "Blackbeard", "Glaz", "Fuze", "Buck", "Recruit"};
    private final String[] def = {"Jäger", "Bandit", "Rook", "Doc", "Pulse", "Castle", "Tachanka", "Kapkan", "Frost", "Smoke", "Mute", "Caveira", "Echo", "Valkyrie", "Mira", "Recruit"};

    private List<String> current;


    private String getRandOp() {

        int rand = new Random().nextInt(15);

        String out = current.get(rand);
        current.remove(rand);

        return out;

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
            return;
        }

        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(MSGS.error.setDescription("You need to be in a voice channel to use this command!").build()).queue();
            return;
        }

        current = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String title;
        Color color;

        switch (args[0]) {

            case "d":
            case "def":
                Arrays.stream(def).forEach(s -> current.add(s));
                title = "DEFENDERS";
                color = new Color(0xFF7700);
                break;

            case "a":
            case "attack":
            case "atc":
                Arrays.stream(attc).forEach(s -> current.add(s));
                title = "ATTACKERS";
                color = new Color(0x0043FF);
                break;

            default:
                event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
                return;

        }

        event.getMember().getVoiceState().getChannel().getMembers()
                .forEach(m -> sb.append(
                        "**" + m.getEffectiveName() + "**  -  **`" + getRandOp() + "`**\n"
                ));

        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(color)
                .setTitle(title, null)
                .setDescription(sb.toString())
                .build()
        ).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE:\n" +
                "**rand6 d**  -  `Get random ops for defenders side`\n" +
                "**rand6 a**  -  `Get random ops for attackers side`";
    }

    @Override
    public String description() {
        return "Role random Rainbow Six Siege operators for voice members";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }
}
