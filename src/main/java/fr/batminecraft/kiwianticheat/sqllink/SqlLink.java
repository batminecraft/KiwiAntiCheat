package fr.batminecraft.kiwianticheat.sqllink;

import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import org.bukkit.Bukkit;

import java.sql.*;

public class SqlLink {
    public static String IP = "";
    public static String PORT = "";
    public static String DB = "";
    public static String USER = "";
    public static String PASS = "";
    public static Boolean ENABLED = false;

    public static Boolean testLink() {
        if(!ENABLED) {
            Bukkit.getServer().getConsoleSender().sendMessage("The SQL Link is disabled !");
            PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + "The SQL Link is disabled !");
            return false;
        }

        if(!verifyConfigRequirements()) {
            Bukkit.getServer().getConsoleSender().sendMessage("The configuration is not complete !");
            PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + "The configuration is not complete !");
            return false;
        }

        if(getConnection() != null) {
            return true;
        }

        return false;
    }

    private static Boolean verifyConfigRequirements() {
        return !IP.isEmpty() && !PORT.isEmpty() && !DB.isEmpty() && !USER.isEmpty();

    }

    public static Boolean setupDataBase() {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery("SHOW TABLES LIKE 'detections'");
            if (!resultSet.next()) {
                statement.executeUpdate("CREATE TABLE detections (id INT(255) NOT NULL AUTO_INCREMENT, type VARCHAR(255) NOT NULL, content TEXT NOT NULL, PRIMARY KEY (id))");
            }
            return true;
        } catch (SQLException e) {
            PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + e.getMessage());
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                return true;
            } catch (SQLException e) {
                PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + e.getMessage());
                return false;
            }
        }
    }

    public static Connection getConnection() {
        if(!verifyConfigRequirements()) {
            return null;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB + "?useSSL=false";
            String username = USER;
            String password = PASS;

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + e.getMessage());
            return null;
        } catch (SQLException e) {
            PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + e.getMessage());
            return null;
        }
    }

}
