package commands.etc;

import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zekro on 06.06.2017 / 10:52
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class Rand6 implements Command {

    private String[] attc = {"Blitz", "IQ", "Twitch", "Montagne", "Ashe", "Thermite", "Sledge", "Thatcher", "Capitão", "Jackal", "Hibana", "Blackbeard", "Glaz", "Fuze", "Buck", "Recruit"};
    private String[] def = {"Jäger", "Bandit", "Rook", "Doc", "Pulse", "Castle", "Tachanka", "Kapkan", "Frost", "Smoke", "Mute", "Caveira", "Echo", "Valkyrie", "Mira", "Recruit"};

    private List<String> current;

    private File saveFile;

    private String getRandOp() {

        int rand = new Random().nextInt(current.size());

        String out = current.get(rand);
        current.remove(rand);

        return out;

    }

    private void reroll(Member member, MessageReceivedEvent event) throws IOException {

        BufferedReader fr;
        Date date = new Date();

        HashMap<Member, String> rollMap = new HashMap<>();

        if (saveFile.exists()) {
            fr = new BufferedReader(new FileReader(saveFile));
            fr.lines().forEach(s -> rollMap.put(event.getGuild().getMemberById(s.split(":")[0]), s.split(":")[1]));
            fr.close();
        }

        if ((date.getTime()) < Long.parseLong((rollMap.get(member) == null) ? (date.getTime()-1) + "" : rollMap.get(member))) {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("Your reroll is still consumed.\nReset in `" + timeToReset(rollMap.get(member)) + "`.").build()).queue();
            return;
        } else {
            rollMap.put(member, (date.getTime() + 12*60*60*1000) + "");
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(member.getAsMention() + " USED A REROLL. (Reset in `" + timeToReset(rollMap.get(member)) + "`)\n\nYour new operator, " + member.getAsMention() + ", is `" + getRandOp() + "`").build()).queue();
        }

        FileWriter fw = new FileWriter(saveFile);

        rollMap.keySet().stream().forEach(m -> {
            try {
                fw.write(m.getUser().getId() + ":" + rollMap.get(m) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                event.getTextChannel().sendMessage(MSGS.error().setDescription("AN ERROR OCCURED WHILE WRITING SAVE FILE...").build()).queue();
            }
        });

        fw.close();

    }

    private void resetReroll(Member member, MessageReceivedEvent event) throws IOException {

        BufferedReader fr;
        HashMap<Member, String> rollMap = new HashMap<>();

        if (saveFile.exists()) {
            fr = new BufferedReader(new FileReader(saveFile));
            fr.lines().forEach(s -> rollMap.put(event.getGuild().getMemberById(s.split(":")[0]), s.split(":")[1]));
            fr.close();
        }

        if (rollMap.containsKey(member)) {
            rollMap.remove(member);
            rollMap.forEach((member1, s) -> System.out.println(member1.getEffectiveName() + " - " + s));
            FileWriter fw = new FileWriter(saveFile);

            rollMap.keySet().forEach(m -> {
                try {
                    fw.write(m.getUser().getId() + ":" + rollMap.get(m) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    event.getTextChannel().sendMessage(MSGS.error().setDescription("AN ERROR OCCURED WHILE WRITING SAVE FILE...").build()).queue();
                }
            });
            fw.close();


            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("**Reseted reroll from " + member.getAsMention() + "**").build()).queue();
        } else
            event.getTextChannel().sendMessage(MSGS.error().setDescription(member.getAsMention() + " has no active reroll timeouts.").build()).queue();

    }

    private String timeToReset(String reset) {
        Date date = new Date();
        long timetoreset = Long.parseLong(reset) - date.getTime();
        return String.format("%d h, %d m",
                TimeUnit.MILLISECONDS.toHours(timetoreset),
                TimeUnit.MILLISECONDS.toMinutes(timetoreset) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timetoreset)));
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        saveFile = new File("SERVER_SETTINGS/" + event.getGuild().getId() + "/r6rerolls");

        String urlS;
        if (!(urlS = SSSS.getR6OPSID(event.getGuild())).equalsIgnoreCase("OFF")) {
            Scanner scanner = new Scanner(new URL(urlS).openStream());
            attc = scanner.nextLine().split(", ");
            def = scanner.nextLine().split(", ");
            scanner.close();
        }

        if (args.length < 1) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
            return;
        }

        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(MSGS.error().setDescription("You need to be in a voice channel to use this command!").build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        String title;
        Color color;

        event.getMessage().delete().queue();

        switch (args[0]) {

            case "d":
            case "def":
                current = new ArrayList<>();
                Arrays.stream(def).forEach(s -> current.add(s));
                title = "DEFENDERS";
                color = new Color(0xFF7700);
                break;

            case "a":
            case "attack":
            case "atc":
                current = new ArrayList<>();
                Arrays.stream(attc).forEach(s -> current.add(s));
                title = "ATTACKERS";
                color = new Color(0x0073FF);
                break;

            case "r":
            case "reroll":
            case "re":
                if (args.length < 2) {
                    reroll(event.getMember(), event);
                    return;
                }
                reroll(event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)), event);
                return;

            case "setops":
                if (args.length > 1) {
                    SSSS.setR6OPSID(args[1], event.getGuild());
                    event.getTextChannel().sendMessage(MSGS.success().setDescription("Successfully set Ops source URL to `" + args[1] + "`.").build()).queue();
                } else {
                    event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
                }
                return;

            case "listops":
            case "getops":
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .addField("ATTACKERS", Arrays.toString(attc).replace("[", "").replace("]", ""), false)
                        .addField("DEFENDERS", Arrays.toString(def).replace("[", "").replace("]", ""), false)
                        .build()).queue();
                return;

            case "rules":
                event.getTextChannel().sendMessage(
                        ":game_die:  __**RANDOM6SIEGE©  -  \"Rules\"**__  :game_die:\n\n" +
                             "Before each round, every player in the current voice channel get's automatically assigned an operator randomly by the command `-r6 a` (*for attackers side*) or `-r6 d` (*for defenders side*).\n" +
                             "Is an operator still picked by another random player, you have to pick a **Recruit¹**. Same applies if you don't own the assigned operator.\n\n" +
                             "Achieved a player in a RANDOM6SIEGE© round an **ace²**, so he is able to chose all operators for the next round for every player, or if he want's, he can get a special reroll not appending on the normal rerolls regenrating after 24 hours, with that he is also able to reroll for another player, if he wants or not.\n\n" +
                             "Every player has the chance to reroll his assigned operator **every 24 hours**. Use the `-r6 r <@mention>` command to assign a new operator to the person who rerolls. **This is only available each 24 hours!**\n\n" +
                             "Also ace rerolls are only available for 24 hours.\n\n" +
                             "___\n\n" +
                             "*¹ Recruit can be played with every available combination of weapons and gadgets. That is also valid for every other assigned operator.*\n\n" +
                             "*² Ace means that all enemies got killed by one player, also if one or more enemies left the match. (That does not count if there are only less than 3 enemies in the enemy team.)*"
                ).queue();
                return;

            case "list":
            case "rerolls":
            case "rlist":
                if (saveFile.exists()) {
                    HashMap<Member, String> rollMap = new HashMap<>();
                    StringBuilder outStr = new StringBuilder();
                    if (saveFile.exists()) {
                        BufferedReader fr = new BufferedReader(new FileReader(saveFile));
                        fr.lines().forEach(s -> rollMap.put(event.getGuild().getMemberById(s.split(":")[0]), s.split(":")[1]));
                        fr.close();
                        rollMap.keySet().forEach(k ->
                            outStr.append("**" + k.getEffectiveName() + "**  -  *expires:*  `" + timeToReset(rollMap.get(k)) + "`\n")
                        );
                        event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("**USED REROLLS:**\n\n" + outStr.toString()).build()).queue();
                    }
                }
                return;

            case "reset":
                if (args.length < 2) {
                    event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
                    return;
                }
                if (core.Perms.check(2, event)) return;

                resetReroll(event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)), event);

                return;


            default:
                event.getTextChannel().sendMessage(MSGS.error().setDescription(help()).build()).queue();
                return;
        }

        event.getMember().getVoiceState().getChannel().getMembers().stream()
                .filter(m -> !m.getUser().isBot())
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
                "**rand6 rules**  -  `Display RANDOM6SIEGE© game rules`\n" +
                "**rand6 d**  -  `Get random ops for defenders side`\n" +
                "**rand6 a**  -  `Get random ops for attackers side`\n" +
                "**rand6 r (<@mention>)**  -  `Use a reroll`\n" +
                "**rand6 list**  -  `List all outtiming rerolls`\n" +
                "**rand6 reset <@mention>**  -  `Reset an outtiming reroll`\n" +
                "**rand6 setops <URL>**  -  `Set URL for a online text file with operators list for randomizing - set to OFF for default list`\n" +
                "**rand6 listops**  -  `Get current operators lists`";
    }

    @Override
    public String description() {
        return "Play RANDOM6SIEGE© - Role random Rainbow Six Siege operators for voice members";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }

    @Override
    public int permission() {
        return 0;
    }
}
