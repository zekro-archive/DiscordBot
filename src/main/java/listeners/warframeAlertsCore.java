package listeners;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class warframeAlertsCore {

    static String APIALERTLIST = "";

    static ArrayList<String[]> getAlerts() {

        ArrayList<String[]> output = new ArrayList<>();

        try {

            URL url = new URL("https://deathsnacks.com/wf/data/alerts_raw.txt");
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNextLine()) {
                String[] out = scanner.nextLine().split("\\|");
                output.add(out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    static String unixTimeFormat(long timeStamp) {

        Date time = new Date(timeStamp*1000);
        DateFormat df = new SimpleDateFormat("HH:mm:ss z (dd/MM)");
        return df.format(time);
    }

    static boolean checkForListUpdate() throws IOException {
        URL url = new URL("https://deathsnacks.com/wf/data/alerts_raw.txt");
        Scanner scanner = new Scanner(url.openStream());

        String currentList = "";
        while (scanner.hasNextLine()) {
            currentList += scanner.nextLine();
        }

        if (currentList.equals(APIALERTLIST)) {
            return false;
        } else {
            URL url2 = new URL("https://deathsnacks.com/wf/data/alerts_raw.txt");
            Scanner scanner2 = new Scanner(url.openStream());
            APIALERTLIST = "";
            while (scanner2.hasNextLine()) {
                APIALERTLIST += scanner2.nextLine();
            }
            return true;
        }
    }

    static void outputMessage() {

        for (String[] e : getAlerts()) {
            System.out.println(
                    e[1] + " (" + e[2] + ")" +
                            " | " + e[3] +
                            " | " + e[4] +
                            " | " + "Lvl: " + e[5] + "-" + e[6] +
                            " | " + "Loot: " + e[9] +
                            " | " + "Finish: " + unixTimeFormat(Long.parseLong(e[8]))
            );
        }

    }

    static ArrayList<String[]> getFilteredAlerts(String[] filter, ArrayList<String[]> alerts) {

        ArrayList<String[]> output = new ArrayList<>();
        for (String[] e : alerts) {
            if (Arrays.stream(filter).parallel().anyMatch(e[9]::contains)) {
                output.add(e);
            }
        }

        return output;
    }

    static String getAlertsAsMessage(ArrayList<String[]> alertsList) {

        String output = "\n\n**WARFRAME ALERTS**\n\n";
        for (String[] e : alertsList) {

            output +=   "Mission: " + e[1] + " (" + e[2] + ")" + "\n" +
                        "Mission type: " + e[3] + "\n" +
                        "Fraction: " + e[4] + "\n" +
                        "Level: " + e[5] + " - " + e[6] + "\n" +
                        "Loot: **" + e[9] + "**\n" +
                        "Expires: **" + unixTimeFormat(Long.parseLong(e[8])) + "** \n\n"
            ;


            /*System.out.println(
                            e[1] + " (" + e[2] + ")" +
                            " | " + e[3] +
                            " | " + e[4] +
                            " | " + "Lvl: " + e[5] + "-" + e[6] +
                            " | " + "Loot: " + e[9] +
                            " | " + "Finish: " + unixTimeFormat(Long.parseLong(e[8]))
            );*/
        }

        return output;
    }

    static String[] getFilter() throws IOException {
        URL url = new URL("https://dl.dropboxusercontent.com/s/wvaumg7lxc27hr5/dcbot_waframealertconfig.txt");
        Scanner scanner = new Scanner(url.openStream());
        String[] filter = scanner.nextLine().split(",");
        return filter;
    }

}
