package fr.batminecraft.kiwianticheat;

import fr.batminecraft.kiwianticheat.automod.AutoModSystem;
import fr.batminecraft.kiwianticheat.commands.KACMCTabCompleter;
import fr.batminecraft.kiwianticheat.commands.KiwiAntiCheatMainCommand;
import fr.batminecraft.kiwianticheat.config.ConfigurationHandler;
import fr.batminecraft.kiwianticheat.logger.KiwiLogSystem;
import fr.batminecraft.kiwianticheat.logger.KiwiLogger;
import fr.batminecraft.kiwianticheat.report.commands.ReportCommand;
import fr.batminecraft.kiwianticheat.report.commands.ReportsCommand;
import fr.batminecraft.kiwianticheat.report.guis.AdminGui;
import fr.batminecraft.kiwianticheat.report.guis.ReportGui;
import fr.batminecraft.kiwianticheat.sensors.*;
import fr.batminecraft.kiwianticheat.sensors.KillAuraSensor;
import fr.batminecraft.kiwianticheat.sqllink.SqlLink;
import fr.batminecraft.kiwianticheat.topluck.OpenTopLuckCommand;
import fr.batminecraft.kiwianticheat.topluck.TopLuckSystem;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import fr.batminecraft.kiwianticheat.utils.UtilsEvents;
import fr.batminecraft.kiwianticheat.webhook.discord.DiscordWebHookLink;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.PatternSyntaxException;

public class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        getLogger().info("_______________________________");
        getLogger().info(" ");
        getLogger().info("Loading plugin infos...");
        DiscordWebHookLink.loading();
        PluginInfos.Refresh();
        this.saveDefaultConfig();
        ConfigurationHandler.reload(this);
        getLogger().info("Registering plugin events...");
        getServer().getPluginManager().registerEvents(new UtilsEvents(), this);
        getServer().getPluginManager().registerEvents(new FlySensor(), this);
        getServer().getPluginManager().registerEvents(new AutoClickSensor(), this);
        getServer().getPluginManager().registerEvents(new KillAuraSensor(), this);
        getServer().getPluginManager().registerEvents(new KnockbackSensor(), this);
        getServer().getPluginManager().registerEvents(new NoFallSensor(), this);
        getServer().getPluginManager().registerEvents(new SpeedHackSensor(), this);
        getServer().getPluginManager().registerEvents(new ReachSensor(), this);
        getServer().getPluginManager().registerEvents(new TopLuckSystem(), this);
        getServer().getPluginManager().registerEvents(new AutoModSystem(), this);
        getServer().getPluginManager().registerEvents(new ReportGui(), this);
        getServer().getPluginManager().registerEvents(new AdminGui(), this);
        getLogger().info("Registering plugin commands...");
        getCommand("topluck").setExecutor(new OpenTopLuckCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportsCommand());
        getCommand("kiwianticheat").setExecutor(new KiwiAntiCheatMainCommand());
        getCommand("kiwianticheat").setTabCompleter(new KACMCTabCompleter());
        getLogger().info("Initialzing database...");
        new MessageTask().runTaskTimer(this, 0L, 20L * 60L * 10L);
        if(SqlLink.ENABLED) {
            if(SqlLink.testLink()) {
                if(SqlLink.setupDataBase()) {
                    getLogger().info("Sql requirements was loaded successful !");
                } else {
                    getLogger().info("Error when setup Sql link ! Go to K.A.C error file !");
                }
            } else {
                getLogger().info("Error when testing MySQL/MariaDB link ! Go to K.A.C error file !");
            }
        } else {
            getLogger().info("Database is not enabled");
        }
        getLogger().info("_______________________________");
        getLogger().info("The plugin is activated");

        TopLuckSystem.startLogger();
        FlySensor.start();
    }

    private class MessageTask extends BukkitRunnable {
        @Override
        public void run() {
            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.sendMessage("  §8§m§l§6-------========§4§lImportant§8§m§l§6========-------");
                p.sendMessage("§8 ");
                p.sendMessage("   " + PluginInfos.PREFIX + "Information importante sur les bugs");
                p.sendMessage("§8 ");
                p.sendMessage("   " + "§7§lVous avez été détecté par l'anticheat ? Cela était une erreur ? §a§nAlors§r §8»");
                p.sendMessage("§8 ");
                p.sendMessage("   " + "§7§lSignalez le bug sur §9Discord §f(rubrique Modération > Signalements de bugs).");
                p.sendMessage("   " + "§7§nCela nous aidera vraiment à améliorer votre expérience de jeu ! Merci !");
                p.sendMessage("§8 ");
                p.sendMessage("  §8§m§l§6-------========§4§lImportant§8§m§l§6========-------");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }

        }
    }

    @Override
    public void onDisable() {
        getLogger().info("The plugin has been deactivated");
        DiscordWebHookLink.disabling();
    }

    @Override
    public void onLoad() {
        getLogger().info("Loading plugin and her classes...");
        instance = this;
    }




}