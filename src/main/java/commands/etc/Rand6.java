package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
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

        int rand = new Random().nextInt(current.size());

        String out = current.get(rand);
        current.remove(rand);

        return out;

    }

    private void reroll(Member member, MessageReceivedEvent event) throws IOException {

        File f = new File("SERVER_SETTINGS/" + event.getGuild().getId() + "/r6rerolls");
        BufferedReader fr;
        Date date = new Date();

        HashMap<Member, String> rollMap = new HashMap<>();

        if (f.exists()) {
            fr = new BufferedReader(new FileReader(f));
            fr.lines().forEach(s -> rollMap.put(event.getGuild().getMemberById(s.split(":")[0]), s.split(":")[1]));
            fr.close();
        }

        if ((date.getTime()) < Long.parseLong((rollMap.get(member) == null) ? (date.getTime()-1) + "" : rollMap.get(member))) {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("Your reroll is still consumed.").build()).queue();
            return;
        } else {
            rollMap.put(member, (date.getTime() + 24*60*60*1000) + "");
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(member.getAsMention() + " USED A REROLL.").build()).queue();
        }

        FileWriter fw = new FileWriter(f);

        rollMap.keySet().stream().forEach(m -> {
            try {
                fw.write(m.getUser().getId() + ":" + rollMap.get(m) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                event.getTextChannel().sendMessage(MSGS.error.setDescription("AN ERROR OCCURED WHILE WRITING SAVE FILE...").build()).queue();
            }
        });

        fw.close();

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

        event.getMessage().delete().queue();

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
                color = new Color(0x0073FF);
                break;

            case "r":
            case "reroll":
            case "re":
                if (args.length < 2) {
                    event.getTextChannel().sendMessage(MSGS.error.setDescription(help()).build()).queue();
                    return;
                }
                reroll(event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)), event);
                return;

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
