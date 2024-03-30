package fr.batminecraft.kiwianticheat.report.guis;

import fr.batminecraft.kiwianticheat.report.ReportEngine;
import fr.batminecraft.kiwianticheat.report.ReportPacket;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportGui implements Listener {

    public static void openReportGUI(Player reporter, Player reportedPlayer) {
        Inventory gui = Bukkit.createInventory(null, 27, "§4§lRapport: §7§o" + reportedPlayer.getName());
        for (int i = 0; i < 27; i++) {
            if (i % 2 == 0) {
                gui.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
            } else {
                gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
        gui.setItem(11, createItem(Material.DIAMOND_BLOCK, "§6§l§nBlocks"));
        gui.setItem(12, createItem(Material.IRON_SWORD, "§6§l§nCombat"));
        gui.setItem(13, createItem(Material.FEATHER, "§6§l§nMovement"));
        gui.setItem(14, createItem(Material.WRITABLE_BOOK, "§6§l§nTextual"));
        gui.setItem(15, createItem(Material.CHEST, "§6§l§nAutre"));

        reporter.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("§4§lRapport:")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            Player reporter = (Player) event.getWhoClicked();

            Player reportedPlayer = Bukkit.getServer().getPlayer(event.getView().getTitle().replaceAll("§4§lRapport: §7§o", ""));
            String categoryName = event.getCurrentItem().getItemMeta().getDisplayName();
            switch (categoryName) {
                case "§6§l§nBlocks":
                    openSubReportGUI(reporter, reportedPlayer,"Blocks");
                    break;
                case "§6§l§nCombat":
                    openSubReportGUI(reporter, reportedPlayer, "Combat");
                    break;
                case "§6§l§nMovement":
                    openSubReportGUI(reporter, reportedPlayer, "Movement");
                    break;
                case "§6§l§nTextual":
                    openSubReportGUI(reporter, reportedPlayer, "Textual");
                    break;
                case "§6§l§nAutre":
                    openSubReportGUI(reporter, reportedPlayer, "Other");
                    break;
                case "§2§lRetour":
                    openReportGUI(reporter, reportedPlayer);
                    break;

            }
            if(categoryName.startsWith("§c§n")) {
                LocalDateTime hour = LocalDateTime.now();
                String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                ReportPacket rPacket = new ReportPacket(reporter.getName(), reportedPlayer.getName(), categoryName.replaceAll("§c§n", ""), decimalDate);
                reporter.sendMessage("§7=*=========================================*=");
                reporter.sendMessage("");
                reporter.sendMessage("          " + PluginInfos.PREFIX.replaceAll(" §7» ", ""));
                reporter.sendMessage("");
                reporter.sendMessage("§aMerci pour votre rapport ! Celui-ci sera transmit au staff, il sera traité d'ici peu.");
                reporter.sendMessage("");
                reporter.sendMessage("§7§nRécapitulatif de votre rapport :");
                reporter.sendMessage("");
                reporter.sendMessage("§8 - §cPseudo du joueur : §6" + reportedPlayer.getName());
                reporter.sendMessage("§8 - §cRaison du rapport : §6" + categoryName.replaceAll("§c§n", ""));
                reporter.sendMessage("");
                reporter.sendMessage("§7=*=========================================*=");
                ReportEngine.report(rPacket);
                reporter.closeInventory();
            }
        }
    }

    public void openSubReportGUI(Player reporter, Player reportedPlayer, String category) {
        Inventory gui = Bukkit.createInventory(null, 27, "§4§lRapport: §7§o" + reportedPlayer.getName());
        for (int i = 0; i < 27; i++) {
            if (i % 2 == 0) {
                gui.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
            } else {
                gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
        gui.setItem(22, createItem(Material.ARROW, "§2§lRetour"));
        switch (category) {
            case "Blocks":
                gui.setItem(11, createItem(Material.DIAMOND_BLOCK, "§c§nFastplace"));
                gui.setItem(12, createItem(Material.IRON_BLOCK, "§c§nFastbreak"));
                gui.setItem(13, createItem(Material.BRICK, "§c§nScaffold"));
                gui.setItem(14, createItem(Material.DIAMOND_ORE, "§c§nX-ray"));
                gui.setItem(15, createItem(Material.BARRIER, "§4§lVide"));
                break;
            case "Combat":
                gui.setItem(11, createItem(Material.DIAMOND_SWORD, "§c§nKill Aura"));
                gui.setItem(12, createItem(Material.BOW, "§c§nReach"));
                gui.setItem(13, createItem(Material.DIAMOND_AXE, "§c§nAutoclick"));
                gui.setItem(14, createItem(Material.IRON_SWORD, "§c§nAnti Knockback"));
                gui.setItem(15, createItem(Material.DIAMOND_SWORD, "§c§nAimbot"));
                break;
            case "Movement":
                gui.setItem(11, createItem(Material.ELYTRA, "§c§nFly"));
                gui.setItem(12, createItem(Material.FEATHER, "§c§nSpeedhack"));
                gui.setItem(13, createItem(Material.MINECART, "§c§nVelocity"));
                gui.setItem(14, createItem(Material.WATER_BUCKET, "§c§nJesus"));
                gui.setItem(15, createItem(Material.STRING, "§c§nSpider"));
                break;
            case "Textual":
                gui.setItem(11, createItem(Material.BOOK, "§c§nFlood"));
                gui.setItem(12, createItem(Material.BOOK, "§c§nInsultes"));
                gui.setItem(13, createItem(Material.BOOK, "§c§nSpam"));
                gui.setItem(14, createItem(Material.BOOK, "§c§nDiffamation"));
                gui.setItem(15, createItem(Material.BOOK, "§c§nHarcèlement"));
                break;
            case "Other":
                gui.setItem(11, createItem(Material.ENCHANTED_BOOK, "§c§nTransgression"));
                gui.setItem(12, createItem(Material.STICK, "§c§nContournement de Sanction"));
                gui.setItem(13, createItem(Material.ENDER_PEARL, "§c§nUsebug"));
                gui.setItem(14, createItem(Material.PAPER, "§c§nAutre"));
                gui.setItem(15, createItem(Material.BARRIER, "§4§lVide"));
                break;
        }
        reporter.openInventory(gui);
    }

    public static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
