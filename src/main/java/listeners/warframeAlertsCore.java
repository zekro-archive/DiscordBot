package listeners;

import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.STATICS;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class warframeAlertsCore {

    public static String APIALERTLIST = "";
    static String alertsServerID = "266589518537162762";
    static String alertsChannelName = "warframealerts";

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

        Date time = new Date(timeStamp * 1000);
        DateFormat df = new SimpleDateFormat("HH:mm:ss z (dd/MM)");
        return df.format(time);
    }

    public static boolean[] checkForListUpdate() throws IOException {

        boolean isUpdate = false;
        boolean isDisappeared = false;

        String currentList = "";

        for (String[] a : getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts())) {
            for (String b : a) {
                currentList += b + "; ";
            }
        }

        if (currentList.equals(APIALERTLIST)) {
            isUpdate = false;
        } else {

            if (APIALERTLIST.length() > currentList.length()) {
                isDisappeared = true;
                //System.out.println(isDisappeared + " - " + APIALERTLIST.length() + " > " + currentList.length());

                APIALERTLIST = "";
                for (String[] a : getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts())) {
                    for (String b : a) {
                        APIALERTLIST += b + "; ";
                    }
                }

                boolean[] out = {isUpdate, isDisappeared};
                return out;
            }

            APIALERTLIST = "";
            for (String[] a : getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts())) {
                for (String b : a) {
                    APIALERTLIST += b + "; ";
                }
            }
            isUpdate = true;
        }

        boolean[] out = {isUpdate, isDisappeared};
        return out;
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
                output += "|  Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                        "|  Mission type:   " + e[3] + " **" + e[10] + "**\n" +
                        "|  Fraction:   " + e[4] + "\n" +
                        "|  Level:   " + e[5] + " - " + e[6] + "\n" +
                        "|  Loot:   **" + e[9] + "**\n" +
                        "|  Expires:   **" + getTimeDiff(Long.parseLong(e[8])) + "**\n\n"
                ;
            } catch (Exception exception) {
                output += "|  Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                        "|  Mission type:   " + e[3] + "\n" +
                        "|  Fraction:   " + e[4] + "\n" +
                        "|  Level:   " + e[5] + " - " + e[6] + "\n" +
                        "|  Loot:   **" + e[9] + "**\n" +
                        "|  Expires:   **" + getTimeDiff(Long.parseLong(e[8])) + "** \n\n"
                ;
            }

        }

        output += "\n**Item Information**\n\n";

        for (String[] e : alertsList) {
            if (e[9].contains("-"))
                output += "http://warframe.wikia.com/wiki/" + transToLink(e[9]) + "\n\n";
        }

        return output;
    }

    public static String transToLink(String input) {

        String out = "";

        out = input.split("-")[1].substring(1);

        if (input.contains("Blueprint")) {
            out = out.substring(0, out.length() - 10);
        }

        out = out.replaceAll(" ", "_");

        return out;
    }

    public static String getTimeDiff(long dateInUnix) {

        Date dateForm = new Date();

        long diffSec = dateInUnix - (dateForm.getTime() / 1000);
        float diffMin = diffSec / 60f;
        double seconds = Math.floor((diffMin - Math.floor(diffMin)) * 60);

        String out = (int) Math.floor(diffMin) + " min. ";

        if ((int) seconds != 0)
            out += (int) seconds + " sec.";

        return out;
    }

    public static void pasteAlertsInChat(ReadyEvent event) {
        try {

            // Normal Update when new alerts appear
            if (checkForListUpdate()[0] && !checkForListUpdate()[1] && getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()).size() != 0 && STATICS.enableWarframeAlerts) {

                MessageHistory history = new MessageHistory(event.getJDA().getGuildById(alertsServerID).getTextChannelsByName(alertsChannelName, false).get(0));
                List<Message> msgs;

                //System.out.println("LÖSCHEN - " + checkForListUpdate()[1] );

                try {

                    msgs = history.retrievePast(1).block();
                    event.getJDA().getGuildById(alertsServerID).getTextChannelsByName(alertsChannelName, false).get(0).deleteMessageById(msgs.get(0).getId()).queue();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                event.getJDA().getGuildById(alertsServerID).getTextChannelsByName(alertsChannelName, false).get(0).sendMessage(
                        getAlertsAsMessage(getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()))
                ).queue();

            }

            // Updating times, updating list without notification in discord if an alert disappears from list
            else if (getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()).size() != 0 && STATICS.enableWarframeAlerts) {

                MessageHistory history = new MessageHistory(event.getJDA().getGuildById(alertsServerID).getTextChannelsByName(alertsChannelName, false).get(0));
                List<Message> msgs;

                //System.out.println("EDITIEREN");

                try {

                    msgs = history.retrievePast(1).block();
                    msgs.get(0).editMessage(
                            getAlertsAsMessage(getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()))
                    ).queue();

                } catch (RateLimitedException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* NORMAL
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
            if (e[9].contains("-"))
                output += "http://warframe.wikia.com/wiki/" + transToLink(e[9]) + "\n\n";
        }

        return output;
    }
    */
}
