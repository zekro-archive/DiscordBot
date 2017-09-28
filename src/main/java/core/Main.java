package core;

import commands.Command;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import commands.administration.*;
import commands.chat.*;
import commands.essentials.*;
import commands.etc.*;
import commands.guildAdministration.*;
import commands.music.*;
import commands.settings.*;
import listeners.*;
import utils.STATICS;


public class Main {

    static JDABuilder builder;

    public static final CommandParser parser = new CommandParser();

    public static HashMap<String, Command> commands = new HashMap<>();

    public static JDA jda;

    public static void main(String[] args) throws IOException {

        StartArgumentHandler.args = args;

        SettingsCore.loadSettings();

        BotStats.load();

        if (!new File("WILDCARDS.txt").exists())
            ServerLimitListener.createTokenList(50);


        try {
            if (!SettingsCore.testForToken()) {
                System.out.println("[ERROR] PLEASE ENTER YOUR DISCORD API TOKEN FROM 'https://discordapp.com/developers/applications/me' IN THE TEXTFILE 'SETTINGS.txt' AND RESTART!");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] PLEASE ENTER YOUR DISCORD API TOKEN FROM 'https://discordapp.com/developers/applications/me' IN THE TEXTFILE 'SETTINGS.txt' AND RESTART!");
            System.exit(0);
        }

        File savePath = new File("saves_playlists");
        if (!savePath.exists() || !savePath.isDirectory()) {
            System.out.println(
                    savePath.mkdir() ? "[INFO] Path \"saves_playlists\" successfully created!" : "[ERROR] Failed to create path \"saves_playlists\"!"
            );
        }

        builder = new JDABuilder(AccountType.BOT)
                .setToken(STATICS.TOKEN)
                .setAudioEnabled(true)
                .setAutoReconnect(true)
                .setStatus(STATICS.STATUS)
                .setGame(STATICS.GAME);

        initializeListeners();
        initializeCommands();

        try {
            builder.buildBlocking();
        } catch (InterruptedException | RateLimitedException | LoginException e) {
            e.printStackTrace();
        }

    }

    private static void initializeCommands() {

        commands.put("ping", new Ping());
        commands.put("cat", new Cat());
        commands.put("8ball", new EightBall());
        commands.put("clear", new Clear());
        commands.put("purge", new Clear());
        commands.put("bjoke", new BJoke());
        commands.put("bj", new BJoke());
        commands.put("help", new Help());
        commands.put("info", new Info());
        commands.put("alerts", new WarframeAlerts());
        commands.put("test", new TestCMD());
        commands.put("ttt", new TTT());
        commands.put("say", new Say());
        commands.put("poll2", new Vote2());
        commands.put("vote2", new Vote2());
        commands.put("stats", new Stats());
        commands.put("joke", new JokeV2());
        commands.put("userinfo", new UserInfo());
        commands.put("user", new UserInfo());
        commands.put("nudge", new Stups());
        commands.put("stups", new Stups());
        commands.put("update", new Update());
        commands.put("restart", new Restart());
        commands.put("kick", new Kick());
        commands.put("vkick", new VoiceKick());
        commands.put("music", new Music());
        commands.put("m", new Music());
        commands.put("dev", new Dev());
        commands.put("stop", new Stop());
        commands.put("moveall", new Moveall());
        commands.put("mvall", new Moveall());
        commands.put("uptime", new Uptime());
        commands.put("botmsg", new Botmessage());
        commands.put("prefix", new Prefix());
        commands.put("joinmsg", new ServerJoinMessage());
        commands.put("leavemsg", new ServerLeftMessage());
        commands.put("permlvl", new PermLvls());
        commands.put("autorole", new AutoRole());
        commands.put("settings", new commands.settings.Settings());
        commands.put("cmdlog", new CmdLog());
        commands.put("speed", new Speedtest());
        commands.put("speedtest", new Speedtest());
        commands.put("quote", new Quote());
        commands.put("r6", new Rand6());
        commands.put("rand6", new Rand6());
        commands.put("r", new Rand6());
        commands.put("mute", new Mute());
        commands.put("log", new Log());
        commands.put("broadcast", new Broadcast());
        commands.put("guilds", new Guilds());
        commands.put("report", new Report());
        commands.put("bug", new Bug());
        commands.put("suggestion", new Bug());
        commands.put("spacer", new Spacer());
        commands.put("botstats", new BotStats());
        commands.put("blacklist", new Blacklist());
        commands.put("count", new Count());
        commands.put("vote", new Vote3());
        commands.put("poll", new Vote3());
        commands.put("counter", new Counter());
        commands.put("c", new Counter());
        commands.put("autochannel", new Autochannel());
        commands.put("donate", new Donate());
        commands.put("support", new Donate());
        commands.put("gif", new Gif());
        commands.put("guild", new Stats());

    }

    private static void initializeListeners() {

        builder .addEventListener(new ReadyListener())
                .addEventListener(new BotListener())
                .addEventListener(new ReconnectListener())
                .addEventListener(new VoiceChannelListener())
                .addEventListener(new GuildJoinListener())
                .addEventListener(new PrivateMessageListener())
                .addEventListener(new ReactionListener())
                .addEventListener(new VkickListener())
                .addEventListener(new ServerLimitListener())
                .addEventListener(new AutochannelHandler())
                .addEventListener(new MuteHanlder())
                .addEventListener(new BotGuildJoinListener());
    }

    public static void handleCommand(CommandParser.CommandContainer cmd) throws ParseException, IOException {

        if (commands.containsKey(cmd.invoke.toLowerCase())) {

            BotStats.commandsExecuted++;
            boolean safe = commands.get(cmd.invoke.toLowerCase()).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke.toLowerCase()).action(cmd.args, cmd.event);
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            }

            List<TextChannel> tcs = cmd.event.getGuild().getTextChannelsByName("cmdlog", true);
            if (tcs.size() > 0) {
                User author = cmd.event.getAuthor();
                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.orange)
                        .setAuthor(author.getName() + "#" + author.getDiscriminator(), null, author.getAvatarUrl())
                        .setDescription("```" + cmd.event.getMessage().getContent() + "```")
                        .setFooter(CoreCommands.getCurrentSystemTime(), null);
                tcs.get(0).sendMessage(eb.build()).queue();
            }
        }
    }

}
