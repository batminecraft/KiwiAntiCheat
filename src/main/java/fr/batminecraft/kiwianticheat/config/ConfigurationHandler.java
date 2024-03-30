package fr.batminecraft.kiwianticheat.config;

import fr.batminecraft.kiwianticheat.Main;
import fr.batminecraft.kiwianticheat.automod.AutoModSystem;
import fr.batminecraft.kiwianticheat.report.ReportEngine;
import fr.batminecraft.kiwianticheat.sensors.*;
import fr.batminecraft.kiwianticheat.sqllink.SqlLink;
import fr.batminecraft.kiwianticheat.topluck.TopLuckSystem;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationHandler {

    public static Boolean reload(Main main) {
        try {
            main.reloadConfig();
            FileConfiguration config = main.getConfig();
            if(config.getBoolean("mysql.enabled")) {
                SqlLink.ENABLED = true;
                SqlLink.IP = config.getString("mysql.connect.ip");
                SqlLink.PORT = config.getString("mysql.connect.port");
                SqlLink.DB = config.getString("mysql.connect.data-base");
                SqlLink.USER = config.getString("mysql.connect.user");
                SqlLink.PASS = config.getString("mysql.connect.password");
            }
            AutoModSystem.RUN = config.getBoolean("services.automod.enabled");
            TopLuckSystem.RUN = config.getBoolean("services.topluck.enabled");
            ReportEngine.RUN = config.getBoolean("services.report.enabled");
            PluginInfos.enableDiscordLink = config.getBoolean("services.d-kiwilink.enabled");

            AutoClickSensor.RUN = config.getBoolean("sensors.autoclick.enabled");
            FlySensor.RUN = config.getBoolean("sensors.fly.enabled");
            KillAuraSensor.RUN = config.getBoolean("sensors.killaura.enabled");
            NoFallSensor.RUN = config.getBoolean("sensors.nofall.enabled");
            ReachSensor.RUN = config.getBoolean("sensors.reach.enabled");
            SpeedHackSensor.RUN = config.getBoolean("sensors.speedhack.enabled");

            PluginInfos.discordWebHookLink = config.getString("d-kiwilink.webhook-token");
            main.saveConfig();
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage("KAC Config error : " + e.getMessage());
            PluginInfos.errorLogger.logError("[ERROR] -> [ConfigurationHandler] -> " + e.getMessage());
            Bukkit.getServer().getConsoleSender().sendMessage("Shutting down KiwiAntiCheat !");
            Bukkit.getPluginManager().disablePlugin(Main.instance);
            return false;
        }

    }
}
