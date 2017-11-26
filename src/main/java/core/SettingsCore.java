package core;


import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.dv8tion.jda.core.entities.Guild;
import org.json.JSONObject;
import utils.STATICS;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * © zekro 2017
 *
 * @author zekro
 */

public class SettingsCore {


    private static File sfile = new File("SETTINGS.txt");


    public static void initialize() {

        JSONObject main = new JSONObject();

        JSONObject mysql = new JSONObject()
                .put("host", "")
                .put("port", "3306")
                .put("username", "")
                .put("password", "")
                .put("database", "zekrobot");

        main    .put("token", "")
                .put("prefix", "-")
                .put("ownerid", "")
                .put("updateinfo", "false")
                .put("musicvolume", 50)
                .put("musicbuffer", 5000)
                .put("mysql", mysql);

        try {
            BufferedWriter br = new BufferedWriter(new FileWriter("SETTINGS.json"));
            br.write(main.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadSettings() throws IOException {

        if (!new File("SETTINGS.json").exists()) {
            initialize();
            System.out.println("Please open \"SETTINGS.json\" file and enter your discord token and owner ID there!");
            System.exit(-1);
        } else {

            BufferedReader br = new BufferedReader(new FileReader("SETTINGS.json"));
            String out = br.lines().collect(Collectors.joining("\n"));

            JSONObject obj = new JSONObject(out);

            STATICS.TOKEN           = obj.getString("token");
            STATICS.PREFIX          = obj.getString("prefix");
            STATICS.BOT_OWNER_ID    = Long.parseLong(obj.getString("ownerid"));
            STATICS.autoUpdate      = Boolean.parseBoolean(obj.getString("updateinfo"));
            STATICS.music_volume    = obj.getInt("musicvolume");
            STATICS.MUSIC_BUFFER    = obj.getInt("musicbuffer");

            JSONObject mysql = obj.getJSONObject("mysql");

            STATICS.SQL_HOST = mysql.getString("host");
            STATICS.SQL_PORT = mysql.getString("port");
            STATICS.SQL_USER = mysql.getString("username");
            STATICS.SQL_PASS = mysql.getString("password");
            STATICS.SQL_DB = mysql.getString("database");

        }

    }

}
