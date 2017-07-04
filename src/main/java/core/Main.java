package core;

import commands.*;
import commands.administration.*;
import commands.chat.*;
import commands.essentials.*;
import commands.etc.*;
import commands.guildAdministration.*;
import commands.music.Music;
import commands.settings.*;
import listeners.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.STATICS;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class Main {

    static JDABuilder builder;

    public static final CommandParser parser = new CommandParser();

    public static HashMap<String, Command> commands = new HashMap<>();

    public static JDA jda;

    public static void main(String[] args) throws IOException {

        startArgumentHandler.args = args;

        settings.loadSettings();

        try {
            if (!settings.testForToken()) {
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

        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(STATICS.TOKEN);
        builder.setAudioEnabled(true);
        builder.setAutoReconnect(true);

        builder.setStatus(STATICS.STATUS);
        builder.setGame(STATICS.GAME);

        initializeListeners();
        initializeCommands();

        try {
            jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }

        SSSS.checkFolders(jda.getGuilds());

    }

    public static void initializeCommands() {

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
        commands.put("test", new testCMD());
        commands.put("ttt", new TTT());
        commands.put("say", new Say());
        commands.put("poll", new Vote2());
        commands.put("vote", new Vote2());
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
        commands.put("tttserver", new tttServerStatus());
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
        commands.put("settings", new Settings());
        commands.put("cmdlog", new CmdLog());
        commands.put("speed", new Speedtest());
        commands.put("speedtest", new Speedtest());
        commands.put("quote", new Quote());
        commands.put("r6", new Rand6());
        commands.put("rand6", new Rand6());
        commands.put("mute", new Mute());
        commands.put("log", new Log());
        commands.put("broadcast", new Broadcast());
        commands.put("guilds", new Guilds());
        commands.put("report", new Report());
        commands.put("bug", new Bug());
        commands.put("suggestion", new Bug());
        commands.put("spacer", new Spacer());

    }

    public static void initializeListeners() {

        builder.addListener(new readyListener());
        builder.addListener(new botListener());
        builder.addListener(new reconnectListener());
        builder.addListener(new voiceChannelListener());
        builder.addListener(new guildJoinListener());
        builder.addListener(new privateMessageListener());
        builder.addListener(new reactionListener());
        builder.addListener(new vkickListener());
        builder.addListener(new botJoinListener());

    }

    public static void handleCommand(CommandParser.CommandContainer cmd) throws ParseException, IOException {

        if (commands.containsKey(cmd.invoke)) {

            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }

        }
    }
}
