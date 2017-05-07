package core;

import commands.*;
import commands.administration.Restart;
import commands.administration.Stop;
import commands.administration.Update;
import commands.administration.testCMD;
import commands.chat.*;
import commands.essentials.*;
import commands.etc.Dev;
import commands.etc.tttServerStatus;
import commands.guildAdministration.Kick;
import commands.guildAdministration.Moveall;
import commands.guildAdministration.VoiceKick;
import commands.music.Music;
import listeners.*;
import net.dv8tion.jda.core.AccountType;
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

    public static void main(String[] args) throws IOException {

        startArgumentHandler.args = args;

        settings.loadSettings();
        if (!settings.testForToken()) {
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
        //builder.setEnableShutdownHook(true);

        builder.setStatus(STATICS.STATUS);
        builder.setGame(STATICS.GAME);

        initializeListeners();
        initializeCommands();
        //settings.initializeSettings();

        try {
            builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }

    }

    public static void initializeCommands() {

        commands.put("ping", new Ping());
        commands.put("cat", new Cat());
        commands.put("8ball", new EightBall());
        commands.put("clear", new Clear());
        commands.put("purge", new Clear());
        commands.put("bjoke", new BJoke());
        commands.put("bj", new BJoke());
        commands.put("c", new BJokeCancle());
        commands.put("help", new Help());
        commands.put("info", new Info());
        commands.put("alerts", new Alerts());
        commands.put("testcmd", new testCMD());
        commands.put("ttt", new TTT());
        commands.put("say", new Say());
        commands.put("poll", new Vote());
        commands.put("vote", new Vote());
        commands.put("stats", new Stats());
        commands.put("joke", new Joke());
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

    }

    public static void initializeListeners() {

        builder.addListener(new readyListener());
        builder.addListener(new botListener());
        builder.addListener(new reconnectListener());
        builder.addListener(new voiceChannelListener());
        builder.addListener(new guildJoinListener());
        builder.addListener(new privateMessageListener());

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
