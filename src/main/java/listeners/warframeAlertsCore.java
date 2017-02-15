package listeners;

import java.io.*;
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

        String currentList = "";

        for (String[] a : getFilteredAlerts(getFilter(), getAlerts())) {
            for (String b : a) {
                currentList += b + "; ";
            }
        }

        if (currentList.equals(APIALERTLIST)) {
            //System.out.println("false");
            return false;
        } else {

            APIALERTLIST = "";
            for (String[] a : getFilteredAlerts(getFilter(), getAlerts())) {
                for (String b : a) {
                    APIALERTLIST += b + "; ";
                }
            }

            //System.out.println("true");
            return true;
        }

        /*
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
        */
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

        DateFormat dateFormatCurrent = new SimpleDateFormat("HH:mm:ss z (dd/MM)");
        Date currentDate = new Date();

        String output = "-------------------------------------------------------------- \n \n" +
                ":small_red_triangle_down:  **WARFRAME ALERTS** :small_red_triangle_down: *" + dateFormatCurrent.format(currentDate) + "* \n\n";

        for (String[] e : alertsList) {


            try {
                output +=   "Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                            "Mission type:   " + e[3] + " **" + e[10] + "**\n" +
                            "Fraction:   " + e[4] + "\n" +
                            "Level:   " + e[5] + " - " + e[6] + "\n" +
                            "Loot:   **" + e[9] + "**\n" +
                            "Expires:   **" + unixTimeFormat(Long.parseLong(e[8])) + "** \n\n"
                ;
            } catch (Exception exeption) {
                output +=   "Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                            "Mission type:   " + e[3] + "\n" +
                            "Fraction:   " + e[4] + "\n" +
                            "Level:   " + e[5] + " - " + e[6] + "\n" +
                            "Loot:   **" + e[9] + "**\n" +
                            "Expires:   **" + unixTimeFormat(Long.parseLong(e[8])) + "** \n\n"
                ;
            }


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