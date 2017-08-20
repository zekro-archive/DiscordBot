package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zekro on 20.08.2017 / 13:31
 * DiscordBot.core
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */
public class MySQL {

    private static String username = "";
    private static String password = "";
    private static String database = "";
    private static String host = "";
    private static String port = "3306";
    private static Connection con;


    public static void test() {
        try {
            DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
