package commands.chatutil;

import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by zekro on 18.03.2017 / 01:29
 * DiscordBot / commands
 * © zekro 2017
 */



public class Joke implements Command {


    private ArrayList<String> getJokes() {

        ArrayList<String> out = new ArrayList<>();

        try {

            URL url = new URL("https://docs.google.com/feeds/download/documents/export/Export?id=" +
                    STATICS.DOCID_jokes +
                    "&exportFormat=txt");
            Scanner scanner = new Scanner(url.openStream());
            String[] jokes = scanner.nextLine().split("###");

            for ( String s : jokes ) {
                out.add(s);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        Random rand = new Random();

        ArrayList<String> jokeCont = getJokes();
        String joke = jokeCont.get(rand.nextInt(jokeCont.size()));

        event.getTextChannel().sendMessage(

                ":smirk_cat:    " + joke + "    :joy_cat: "

        ).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
