package fr.batminecraft.kiwianticheat.report.guis;

import fr.batminecraft.kiwianticheat.report.ReportEngine;
import fr.batminecraft.kiwianticheat.report.ReportPacket;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AdminGui implements Listener {

    public static void openReportListGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§4§nListe des rapports");
        List<ReportPacket> reportList = ReportEngine.reportList;

        for (int i = 0; i < reportList.size(); i++) {
            if(i >= 54) {
                player.sendMessage("§4Nous avons pas pu afficher tout les rapports, supprimez-en pour afficher d'autres rapports !");
                break;
            }
            ReportPacket report = reportList.get(i);
            Player reporter = Bukkit.getServer().getPlayer(report.getReporter());
            Player reported = Bukkit.getServer().getPlayer(report.getReported());
            ItemStack head = createHead(report.getReported(), report.getReporter(), report.getType(), report.getDate(), reported.getUniqueId());
            gui.setItem(i, head);

        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§4§nListe des rapports"))
            return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        event.setCancelled(true);

        String reported = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§c§o", "");
        String reporter = event.getCurrentItem().getItemMeta().getLore().get(0).replaceAll("§6Rapporter:§7 " , "");
        Player reporterPlayer = Bukkit.getServer().getPlayer(reporter);
        Player reportedPlayer = Bukkit.getServer().getPlayer(reported);
        String type = event.getCurrentItem().getItemMeta().getLore().get(1).replaceAll("§6Raison:§7 " , "");
        String date = event.getCurrentItem().getItemMeta().getLore().get(2).replaceAll("§6Date du rapport:§7 " , "");
        if(event.isLeftClick()) {
            if(reportedPlayer != null) {
                event.getWhoClicked().teleport(reportedPlayer);
                event.getWhoClicked().sendMessage("§aVous avez été téléporté au joueur suspect avec succès !");
            } else {
                event.getWhoClicked().sendMessage("§4Le joueur est hors-ligne ou introuvable !");
            }
        } else if(event.isRightClick()) {
            for(ReportPacket p : ReportEngine.reportList) {
                if(Objects.equals(p.getDate(), date) && Objects.equals(p.getReported(), reported) && Objects.equals(p.getReporter(), reporter) && Objects.equals(p.getType(), type)) {
                    ReportEngine.reportList.remove(p);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage("§aCe rapport a bien été supprimé de la liste, vous pourrez le retrouver dans les logs de KiwiAntiCheat !");
                    openReportListGUI((Player) event.getWhoClicked());
                    break;
                }
            }

        }



    }

    private static ItemStack createHead(String reportedPlayerName, String reporterName, String reason, String date, UUID offlineUUID) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName("§c§o" + reportedPlayerName);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(offlineUUID));
        meta.setLore(List.of("§6Rapporter:§7 " + reporterName, "§6Raison:§7 " + reason, "§6Date du rapport:§7 " + date, " ", "§c§oClique droit pour vous téléporter", "§6§oClique gauche pour supprimer le rapport"));
        head.setItemMeta(meta);
        return head;
    }
}
