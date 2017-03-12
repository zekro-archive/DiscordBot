package core;

import commands.*;
import listeners.botListener;
import listeners.reconnectListener;
import listeners.voiceChannelListener;
import listeners.readyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.SECRETS;
import utils.STATICS;

import javax.security.auth.login.LoginException;
import java.text.ParseException;
import java.util.HashMap;

public class Main {

    public static final CommandParser parser = new CommandParser();

    public static HashMap<String, Command> commands = new HashMap<>();

    public static void main(String[] args) {

        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(SECRETS.TOKEN);
        builder.setAudioEnabled(false);
        builder.setAutoReconnect(true);
        //builder.setEnableShutdownHook(true);

        builder.setStatus(STATICS.STATUS);
        builder.setGame(STATICS.GAME);

        builder.addListener(new readyListener());
        builder.addListener(new botListener());
        builder.addListener(new reconnectListener());
        builder.addListener(new voiceChannelListener());

        initializeCommands();

        try {
            JDA jda = builder.buildBlocking();
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

    }

    public static void handleCommand(CommandParser.CommandContainer cmd) throws ParseException {
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
