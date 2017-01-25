import commands.Cat;
import utils.CommandParser;
import commands.Command;
import commands.Ping;
import listeners.readyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Main {

    public static final CommandParser parser = new CommandParser();

    public static HashMap<String, Command> commands = new HashMap<String, Command>();

    public static void main(String[] args) {

        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(SECRETS.TOKEN);
        builder.setAudioEnabled(false);
        builder.setAutoReconnect(true);
        builder.setEnableShutdownHook(true);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);

        builder.addListener(new readyListener());
        builder.addListener(new botListener());

        commands.put("ping", new Ping());
        commands.put("cat", new Cat());

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

    public static void handleCommand(CommandParser.CommandContainer cmd) {
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
