package core;

import java.sql.*;

/**
 * Created by zekro on 25.11.2017 / 13:12
 * DiscordBot.core
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */
public class MySql {

    private static Connection connection;
    private String host;
    private String port;
    private String user;
    private String password;
    private String database;


    public MySql(String host, String port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }


    public static Connection getConn() {
        return connection;
    }

    public MySql connect() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }


    public MySql initialize() {

        connect();

        try {
            connection.prepareStatement("SELECT 1 FROM guilds LIMIT 1").executeQuery();
        } catch (SQLException e) {
            try {
                connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `guilds` (\n" +
                            "  `id` text,\n" +
                            "  `prefix` text,\n" +
                            "  `joinmsg` text,\n" +
                            "  `leavemsg` text,\n" +
                            "  `musicchan` text,\n" +
                            "  `perm1` text,\n" +
                            "  `perm2` text,\n" +
                            "  `lockmusic` tinyint(1) NOT NULL DEFAULT '0',\n" +
                            "  `autorole` text,\n" +
                            "  `vkick` text,\n" +
                            "  `rsops` text,\n" +
                            "  `warframe` text,\n" +
                            "  `blacklist` text\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
                ).execute();
                connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `autochans` (\n" +
                                "  `chan` text,\n" +
                                "  `guild` text,\n" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
                ).execute();
                System.out.println("MySql structure created...");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return this;
    }


    public String getString(String table, String key ,String where, String value) {

        try {
            PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = %s", table, where, value));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getString(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBool(String table, String key ,String where, String value) {
        try {
            PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = %s", table, where, value));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getBoolean(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public MySql setString(String table, String key, String value, String where, String wherevalue) {
        try {
            PreparedStatement ps;
            PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s'", table, where, wherevalue));

            if (check.executeQuery().next())
                ps = connection.prepareStatement(String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'", table, key, value, where, wherevalue));
            else
                ps = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s')", table, where, key, wherevalue, value));

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public MySql setBool(String table, String key, boolean value, String where, String wherevalue) {
        try {
            PreparedStatement ps;
            PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s'", table, where, wherevalue));

            if (check.executeQuery().next())
                ps = connection.prepareStatement(String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'", table, key, value ? 1 : 0, where, wherevalue));
            else
                ps = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s')", table, where, key, wherevalue, value ? 1 : 0));

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public MySql dropEntry(String table, String where, String wherevalue) {
        try {
            PreparedStatement ps;
            PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s'", table, where, wherevalue));

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement(String.format("DELETE FROM %s WHERE %s = '%s'", table, where, wherevalue));
                ps.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

}
