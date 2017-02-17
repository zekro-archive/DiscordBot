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

    public static String APIALERTLIST = "";

    public static ArrayList<String[]> getAlerts() {

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

    public static String unixTimeFormat(long timeStamp) {

        Date time = new Date(timeStamp*1000);
        DateFormat df = new SimpleDateFormat("HH:mm:ss z (dd/MM)");
        return df.format(time);
    }

    public static boolean checkForListUpdate() throws IOException {

        String currentList = "";

        for (String[] a : getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts())) {
            for (String b : a) {
                currentList += b + "; ";
            }
        }

        if (currentList.equals(APIALERTLIST)) {
            //System.out.println("false");
            return false;
        } else {

            APIALERTLIST = "";
            for (String[] a : getFilteredAlerts(getFilter(),getCounterFilter(), getAlerts())) {
                for (String b : a) {
                    APIALERTLIST += b + "; ";
                }
            }

            //System.out.println("true");
            return true;
        }
    }

    public static ArrayList<String[]> getFilteredAlerts(String[] filter, String[] counterFilter, ArrayList<String[]> alerts) {

        ArrayList<String[]> output = new ArrayList<>();
        for (String[] e : alerts) {
            if (Arrays.stream(filter).parallel().anyMatch(e[9]::contains) && !Arrays.stream(counterFilter).parallel().anyMatch(e[9]::contains)) {
                output.add(e);
            }
        }

        return output;
    }

    public static String[] getFilter() throws IOException {
        //URL url = new URL("https://dl.dropboxusercontent.com/s/wvaumg7lxc27hr5/dcbot_waframealertconfig.txt");
        URL url = new URL("https://docs.google.com/feeds/download/documents/export/Export?id=13O2lZ_UemLDkCV8425XHOPSZ3aVoeYmV5cF_vLQAyEY&exportFormat=txt");
        Scanner scanner = new Scanner(url.openStream());
        String[] filter = scanner.nextLine().split(",");
        return filter;
    }

    public static String[] getCounterFilter() throws IOException {
        //URL url = new URL("https://dl.dropboxusercontent.com/s/wvaumg7lxc27hr5/dcbot_waframealertconfig.txt");
        URL url = new URL("https://docs.google.com/feeds/download/documents/export/Export?id=13O2lZ_UemLDkCV8425XHOPSZ3aVoeYmV5cF_vLQAyEY&exportFormat=txt");
        Scanner scanner = new Scanner(url.openStream());
        scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        String[] filter = scanner.nextLine().split(",");
        return filter;
    }

    public static String getAlertsAsMessage(ArrayList<String[]> alertsList) {

        DateFormat dateFormatCurrent = new SimpleDateFormat("HH:mm:ss z (dd/MM)");
        Date currentDate = new Date();

        String output = ":small_red_triangle_down:  **WARFRAME ALERTS** :small_red_triangle_down: *" + dateFormatCurrent.format(currentDate) + "* \n\n";

        for (String[] e : alertsList) {


            try {
                output +=   "|  Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                            "|  Mission type:   " + e[3] + " **" + e[10] + "**\n" +
                            "|  Fraction:   " + e[4] + "\n" +
                            "|  Level:   " + e[5] + " - " + e[6] + "\n" +
                            "|  Loot:   **" + e[9] + "**\n" +
                            "|  Expires:   **" + unixTimeFormat(Long.parseLong(e[8])) + "**\n\n"
                ;
            } catch (Exception exception) {
                output +=   "|  Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                            "|  Mission type:   " + e[3] + "\n" +
                            "|  Fraction:   " + e[4] + "\n" +
                            "|  Level:   " + e[5] + " - " + e[6] + "\n" +
                            "|  Loot:   **" + e[9] + "**\n" +
                            "|  Expires:   **" + unixTimeFormat(Long.parseLong(e[8])) + "** \n\n"
                ;
            }

        }

        output += "\n**Item Information**\n\n";

        for (String[] e : alertsList) {
            output += "http://warframe.wikia.com/wiki/" + transToLink(e[9]) + "\n\n";
        }

        return output;
    }

    public static String transToLink(String input) {

        String out = input.split("-")[1].substring(1);

        if (input.contains("Blueprint")) {
            out = out.substring(0, out.length() - 10);
        }

        out = out.replaceAll(" ", "_");

        return out;
    }
}
