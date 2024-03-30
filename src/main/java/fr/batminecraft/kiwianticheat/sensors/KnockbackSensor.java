package fr.batminecraft.kiwianticheat.sensors;

import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KnockbackSensor implements Listener {
    public static Boolean RUN = false;

    public static void start() {
        RUN = true;
    }

    public static void stop() {
        RUN = false;
    }

    public static void punish(Player player) {
        player.kickPlayer(PluginInfos.PREFIX + "Détecté pour ANTI-KNOCKBACK");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                player.sendMessage(PluginInfos.PREFIX + PluginInfos.WARNING_PREFIX + "§r§7 <> §cDetected player using ANTI-KNOCKBACK : " + player.getDisplayName());
            }
        }
    }

}
