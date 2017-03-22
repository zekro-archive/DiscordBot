package core;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import utils.STATICS;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by zekro on 22.03.2017 / 15:59
 * DiscordBot / core
 * © zekro 2017
 */


public class update {

    private static String versionURL = "https://dl.dropboxusercontent.com/s/kkiugb66b31o5vw/current_version.txt";

    private static boolean checkIfUpdate() {

        String version = STATICS.VERSION;

        try {

            URL url = new URL(versionURL);
            Scanner s = new Scanner(url.openStream());
            version = s.nextLine();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return !STATICS.VERSION.equals(version);

    }


    public static void getUpdate(ReadyEvent event) {

        System.out.println(checkIfUpdate());
        if (checkIfUpdate()) {

            File f = new File("update.py");
            if (!f.exists() || f.isDirectory()) {
                System.out.println(coreCommands.getCurrentSystemTime() + " [INFO] File 'update.py' does not exist! Please download it from 'github.zekro.de/DiscordBot'!");
                return;
            }

            try {

                for ( Guild g : event.getJDA().getGuilds() ) {
                    g.getTextChannelsByName("general", true).get(0).sendMessage(":warning:   The bot will be shut down for a short while for updating! Bye... :^)");
                }

                Runtime.getRuntime().exec("python update.py");
                System.exit(0);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }
}
