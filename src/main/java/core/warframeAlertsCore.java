package core;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.ReadyEvent;
import utils.STATICS;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class warframeAlertsCore {

    public static String APIALERTLIST = "";
    static String alertsChannelName = STATICS.warframeAlertsChannel;


    /**
     * Get all alerts of the API-Alerts-List.
     * @return ArrayList<String[]>
     */
    public static ArrayList<String[]> getAlerts() {

        //System.out.println(getTimeToAppear(1488302994));

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

    /**
     * Check if Alerts appeared or disappeared in the Alerts list.
     * Check if only an Alert disappeared from the list.
     * @return boolean[]
     * @throws IOException
     */
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

    /**
     * Get item filter for alerts out of a google docs document.
     * @return String[]
     * @throws IOException
     */
    public static String[] getFilter() throws IOException {
        URL url = new URL("https://docs.google.com/feeds/download/documents/export/Export?id=" +
                STATICS.DOCID_warframeAlertsFilter +
                "&exportFormat=txt");
        Scanner scanner = new Scanner(url.openStream());
        String[] filter = scanner.nextLine().split(",");
        return filter;
    }


    /**
     * Get the counter filter for items out of the same google docs document
     * @return String[]
     * @throws IOException
     */
    public static String[] getCounterFilter() throws IOException {
        URL url = new URL("https://docs.google.com/feeds/download/documents/export/Export?id=" +
                STATICS.DOCID_warframeAlertsFilter +
                "&exportFormat=txt");
        Scanner scanner = new Scanner(url.openStream());
        scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        String[] filter = scanner.nextLine().split(",");
        return filter;
    }


    /**
     * Get the filtered Alerts list.
     * @param filter
     * @param counterFilter
     * @param alerts
     * @return ArrayList<String[]>
     */
    public static ArrayList<String[]> getFilteredAlerts(String[] filter, String[] counterFilter, ArrayList<String[]> alerts) {

        ArrayList<String[]> output = new ArrayList<>();
        for (String[] e : alerts) {
            if (Arrays.stream(filter).parallel().anyMatch(e[9]::contains) && !Arrays.stream(counterFilter).parallel().anyMatch(e[9]::contains)) {
                output.add(e);
            }
        }



        return output;
    }

    /**
     * Get the time until an Alert will appear as playable in the game.
     * @param dateInUnix
     * @return String
     */
    public static String getTimeToAppear(long dateInUnix) {

        Date dateForm = new Date();

        long diffSec = -((dateForm.getTime() / 1000) - dateInUnix);
        float diffMin = diffSec / 60f;
        double seconds = Math.floor((diffMin - Math.floor(diffMin)) * 60);

        String out = (int) Math.floor(diffMin) + " min. ";

        if ((int) seconds != 0)
            out += (int) seconds + " sec.";

        if (diffSec < 0)
            out = null;

        return out;
    }

    /**
     * Get the time until an Alert will disappear in the list.
     * @param dateInUnix
     * @return String
     */
    public static String getTimeDiff(long dateInUnix) {

        Date dateForm = new Date();

        long diffSec = dateInUnix - (dateForm.getTime() / 1000);
        float diffMin = diffSec / 60f;
        double seconds = Math.floor((diffMin - Math.floor(diffMin)) * 60);

        String out = (int) Math.floor(diffMin) + " min. ";

        if ((int) seconds != 0)
            out += (int) seconds + " sec.";

        if (diffMin < 5)
            out += "               :warning:";

        return out;
    }

    /**
     * Get the item as link of the warframe wiki.
     * @param input
     * @return String
     */
    public static String transToLink(String input) {

        String out = "";

        out = input.split("-")[1].substring(1);

        if (input.contains("Blueprint")) {
            out = out.substring(0, out.length() - 10);
        }

        out = out.replaceAll(" ", "_");

        return out;
    }

    /**
     * Get the filtered alerts list as message to post into the chat.
     * @param alertsList
     * @return String
     */
    public static String getAlertsAsMessage(ArrayList<String[]> alertsList) {

        DateFormat dateFormatCurrent = new SimpleDateFormat("HH:mm:ss z (dd/MM)");
        Date currentDate = new Date();

        String output = ":small_red_triangle_down:  **WARFRAME ALERTS** :small_red_triangle_down: *" + dateFormatCurrent.format(currentDate) + "* \n\n";

        for (String[] e : alertsList) {

            if (getTimeToAppear(Long.parseLong(e[7])) != null) {

                //System.out.println(getTimeDiff(Long.parseLong(e[7])));
                try {
                    output += "|  Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                            "|  Mission type:   " + e[3] + " **" + e[10] + "**\n" +
                            "|  Fraction:   " + e[4] + "\n" +
                            "|  Level:   " + e[5] + " - " + e[6] + "\n" +
                            "|  Loot:   **" + e[9] + "**\n" +
                            "|  Appears:   **- " + getTimeToAppear(Long.parseLong(e[7])) + "**\n\n"
                    ;
                } catch (Exception exception) {
                    output += "|  Mission:   " + e[1] + " (" + e[2] + ")" + "\n" +
                            "|  Mission type:   " + e[3] + "\n" +
                            "|  Fraction:   " + e[4] + "\n" +
                            "|  Level:   " + e[5] + " - " + e[6] + "\n" +
                            "|  Loot:   **" + e[9] + "**\n" +
                            "|  Appears:   **- " + getTimeToAppear(Long.parseLong(e[7])) + "** \n\n"
                    ;
                }

            } else {

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
        }

        output += "\n**Item Information**\n\n";

        for (String[] e : alertsList) {
            if (e[9].contains("-"))
                output += "http://warframe.wikia.com/wiki/" + transToLink(e[9]) + "\n\n";
        }

        return output;
    }

    /**
     * Finally post the filtered and compiled message in the current chat.
     * @param event
     */
    public static void pasteAlertsInChat(ReadyEvent event) {

        for ( Guild g : event.getJDA().getGuilds() ) {

            try {

                if (g.getTextChannelsByName(alertsChannelName, false).size() > 0) {

                    // Normal Update when new alerts appear
                    if (checkForListUpdate()[0] && !checkForListUpdate()[1] && getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()).size() != 0 && STATICS.enableWarframeAlerts) {

                        MessageHistory history = new MessageHistory(g.getTextChannelsByName(alertsChannelName, false).get(0));
                        List<Message> msgs;

                        //System.out.println("LÖSCHEN - " + checkForListUpdate()[1] );

                        try {

                            msgs = history.retrievePast(1).complete();
                            g.getTextChannelsByName(alertsChannelName, false).get(0).deleteMessageById(msgs.get(0).getId()).queue();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        g.getTextChannelsByName(alertsChannelName, false).get(0).sendMessage(
                                getAlertsAsMessage(getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()))
                        ).queue();

                    }

                    // Updating times, updating list without notification in discord if an alert disappears from list
                    else if (getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()).size() != 0 && STATICS.enableWarframeAlerts) {

                        MessageHistory history = new MessageHistory(g.getTextChannelsByName(alertsChannelName, false).get(0));
                        List<Message> msgs;

                        //System.out.println("EDITIEREN");

                        msgs = history.retrievePast(1).complete();
                        msgs.get(0).editMessage(
                                getAlertsAsMessage(getFilteredAlerts(getFilter(), getCounterFilter(), getAlerts()))
                        ).queue();

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
