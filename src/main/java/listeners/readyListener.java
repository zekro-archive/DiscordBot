package listeners;

import core.update;
import core.warframeAlertsCore;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static listeners.tttServerListener.testOnlineState;

public class readyListener extends ListenerAdapter {

    static Timer timerOnReady;
    static TimerTask timerAction;
    static ReadyEvent readyEvent;

    public static void restartWarframeAlertsCore() {
        timerOnReady.cancel();
        timerAction = new TimerTask() {
            @Override
            public void run() {

                //warframeAlertsCore.pasteAlertsInChat(readyEvent);
                update.getUpdate(readyEvent.getJDA().getGuilds());
                if (!STATICS.TTT_SERVER_IP.equals(""))
                    testOnlineState(readyEvent.getJDA().getGuilds());

            }
        };
        timerOnReady = new Timer();
        timerOnReady.schedule(timerAction, 0, STATICS.refreshTime * 1000);
    }

    private static void handleStartArgs() {

        String[] args = core.startArgumentHandler.args;

        if (args.length > 0) {
            switch (args[0]) {

                case "-restart":
                    for (Guild g : readyEvent.getJDA().getGuilds()) {
                        g.getPublicChannel().sendMessage(
                                ":ok_hand:  Bot successfully restarted!"
                        ).queue();
                    }
                    break;

                case "-update":
                    for (Guild g : readyEvent.getJDA().getGuilds()) {
                        g.getPublicChannel().sendMessage(
                                ":ok_hand:  Bot successfully updated to version v." + STATICS.VERSION + "!\n\n" +
                                        "**Changelogs:** http://github.zekro.de/DiscordBot/blob/master/README.md#latest-changelogs\n" +
                                        "Github Repository: http://github.zekro.de/DiscordBot"
                        ).queue();
                    }
                    break;

            }
        }

    }

    @Override
    public void onReady(ReadyEvent event) {

        StringBuilder sb = new StringBuilder();
        event.getJDA().getGuilds().forEach(guild -> sb.append("|  - \"" + guild.getName() + "\" - {@" + guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator() + "} - [" + guild.getId() + "] \n"));

        System.out.println(
                "\n\n" +
                "#--------------------------------------------------------------------------------- - -  -  -\n" +
                "| zekroBot - v." + STATICS.VERSION + "                              \n" +
                "#--------------------------------------------------------------------------------- - -  -  -\n" +
                "| Running on " + event.getJDA().getGuilds().size() + " guilds.      \n" +
                sb.toString() +
                "#--------------------------------------------------------------------------------- - -  -  -\n\n"
        );

        commands.settings.Botmessage.setSupplyingMessage(event.getJDA());

        readyEvent = event;

        STATICS.lastRestart = new Date();

        handleStartArgs();

        if (!STATICS.enableWarframeAlerts && !System.getProperty("os.name").contains("Windows")) {
            System.out.println("[INFO] System: " + System.getProperty("os.name") + " detected - enabled warframe alerts.");
            STATICS.enableWarframeAlerts = true;
        }

        timerOnReady = new Timer();

        timerAction = new TimerTask() {
            @Override
            public void run() {

                //warframeAlertsCore.pasteAlertsInChat(event);
                update.getUpdate(readyEvent.getJDA().getGuilds());
                if (!STATICS.TTT_SERVER_IP.equals(""))
                    testOnlineState(readyEvent.getJDA().getGuilds());

            }
        };

        timerOnReady.schedule(timerAction, 0, STATICS.refreshTime * 1000);

    }
}
