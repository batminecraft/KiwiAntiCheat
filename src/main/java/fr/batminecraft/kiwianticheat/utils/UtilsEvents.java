package fr.batminecraft.kiwianticheat.utils;

import fr.batminecraft.kiwianticheat.report.ReportEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class UtilsEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("kiwiac.moderation") && !ReportEngine.reportList.isEmpty()) {
            player.sendMessage(PluginInfos.WARNING_PREFIX + " §7- " + PluginInfos.PREFIX + "§d" + ReportEngine.reportList.size() + " §crapport(s) n'ont/a pas été traité(s) ! Éxecutez la commande §d/reports §cpour traiter le(s) rapport(s).");
        }
    }
}
