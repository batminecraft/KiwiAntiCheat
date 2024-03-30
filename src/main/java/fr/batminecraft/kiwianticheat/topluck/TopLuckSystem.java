package fr.batminecraft.kiwianticheat.topluck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.batminecraft.kiwianticheat.logger.formaters.KiwiTopLuckLoggerFormater;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.MainDataTopLuckJson;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.OresMinedList;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.PlayerData;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static fr.batminecraft.kiwianticheat.utils.PluginInfos.main;

public class TopLuckSystem implements Listener {

    public static Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    public static boolean RUN = true;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!RUN) {
            return;
        }
        Player player = event.getPlayer();
        if (!playerDataMap.containsKey(player.getUniqueId())) {
            playerDataMap.put(player.getUniqueId(), new PlayerData());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (inventory.getHolder() instanceof TopLuckInventoryHolder) {
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                String playerName = clickedItem.getItemMeta().getDisplayName();
                player.sendMessage("Clicked player: " + playerName);
            }
        }
    }

    @EventHandler
    public void onPlayerMineBock(BlockBreakEvent e) {
        if(!RUN) {
            return;
        }
        Player player = e.getPlayer();
        Material block = e.getBlock().getType();
        PlayerData pData = playerDataMap.get(player.getUniqueId());
        if(block == Material.STONE || block == Material.DEEPSLATE || block == Material.NETHERRACK) {
            pData.setStoneMined(pData.getStoneMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        } else if(block == Material.ANCIENT_DEBRIS) {
            pData.setNetheriteMined(pData.getNetheriteMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }else if(block == Material.EMERALD_ORE || block == Material.DEEPSLATE_EMERALD_ORE) {
            pData.setEmeraldMined(pData.getEmeraldMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }else if(block == Material.DIAMOND_ORE || block == Material.DEEPSLATE_DIAMOND_ORE) {
            pData.setDiamondMined(pData.getDiamondMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }else if(block == Material.GOLD_ORE || block == Material.DEEPSLATE_GOLD_ORE) {
            pData.setGoldMined(pData.getGoldMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }else if(block == Material.IRON_ORE || block == Material.DEEPSLATE_IRON_ORE) {
            pData.setIronMined(pData.getIronMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }else if(block == Material.COPPER_ORE || block == Material.DEEPSLATE_COPPER_ORE) {
            pData.setCopperMined(pData.getCopperMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }else if(block == Material.COAL_ORE || block == Material.DEEPSLATE_COAL_ORE) {
            pData.setCoalMined(pData.getCoalMined() + 1);
            playerDataMap.replace(player.getUniqueId(), pData);
        }

    }

    public static void startLogger() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime hour = LocalDateTime.now();
                String decimalDate = hour.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PluginInfos.topLuckLogger.logTopLuck(KiwiTopLuckLoggerFormater.formater(new MainDataTopLuckJson(decimalDate), new fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.PlayerData(p.getName(), p.getAddress().getHostString()), new OresMinedList(playerDataMap.get(p.getUniqueId()).getStoneMined(), playerDataMap.get(p.getUniqueId()).getEmeraldMined(), playerDataMap.get(p.getUniqueId()).getNetheriteMined(), playerDataMap.get(p.getUniqueId()).getDiamondMined(), playerDataMap.get(p.getUniqueId()).getGoldMined(), playerDataMap.get(p.getUniqueId()).getIronMined(), playerDataMap.get(p.getUniqueId()).getCopperMined(), playerDataMap.get(p.getUniqueId()).getCoalMined())));
                }
            }
        }.runTaskTimer(main, 0, 6000);
    }

    public static void openTopLuckGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(new TopLuckInventoryHolder(), 54, PluginInfos.PREFIX + "§6Top Luck");

        Comparator<PlayerData> comparator = Comparator.comparingInt(PlayerData::getEmeraldMined)
                .thenComparingInt(PlayerData::getNetheriteMined)
                .thenComparingInt(PlayerData::getDiamondMined)
                .thenComparingInt(PlayerData::getGoldMined)
                .thenComparingInt(PlayerData::getIronMined)
                .thenComparingInt(PlayerData::getCopperMined)
                .thenComparingInt(PlayerData::getCoalMined)
                .reversed();
        playerDataMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .limit(54)
                .forEach(entry -> {
                    if(!Bukkit.getOfflinePlayer(entry.getKey()).isOnline()) {

                    } else {
                        UUID playerId = entry.getKey();
                        PlayerData playerData = entry.getValue();
                        ItemStack playerHead = getPlayerHead(playerId);
                        ItemMeta meta = playerHead.getItemMeta();
                        meta.setDisplayName(ChatColor.of("#48C9B0") + "§l§n" + Bukkit.getOfflinePlayer(entry.getKey()).getName());
                        List<String> lore = new ArrayList<String>();
                        lore.add("§7» " + ChatColor.of("#2ECC71") + "§lMined ore(s) list :");
                        lore.add(" ");
                        lore.add(ChatColor.of("#ABEBC6") + "§nStone/Netherrack blocks mined§r§7: " + playerData.getStoneMined() + " §ffor :");
                        lore.add(" ");
                        lore.add("§7 - " + ChatColor.of("#257D4C") + "§nEmerald ore§r§7: " + playerData.getEmeraldMined());
                        lore.add("§7 - " + ChatColor.of("#31292a") + "§nAncient Debris§r§7: " + playerData.getNetheriteMined());
                        lore.add("§7 - " + ChatColor.of("#4BC5FE") + "§nDiamond ore§r§7: " + playerData.getDiamondMined());
                        lore.add("§7 - " + ChatColor.of("#F1C40F") + "§nGold ore§r§7: " + playerData.getGoldMined());
                        lore.add("§7 - " + ChatColor.of("#CACFD2") + "§nIron ore§r§7: " + playerData.getIronMined());
                        lore.add("§7 - " + ChatColor.of("#B36700") + "§nCopper ore§r§7: " + playerData.getCopperMined());
                        lore.add("§7 - " + ChatColor.of("#34495E") + "§nCoal ore§r§7: " + playerData.getCoalMined());
                        meta.setLore(lore);
                        playerHead.setItemMeta(meta);
                        inventory.addItem(playerHead);
                    }

                });

        player.openInventory(inventory);
    }

    private static ItemStack getPlayerHead(UUID playerId) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerId));
        playerHead.setItemMeta(meta);
        return playerHead;
    }

    private class PlayerData {
        private int stoneMined = 0;
        private int coalMined = 0;
        private int copperMined = 0;
        private int ironMined = 0;
        private int goldMined = 0;
        private int diamondMined = 0;
        private int emeraldMined = 0;
        private int netheriteMined = 0;

        public int getStoneMined() {
            return stoneMined;
        }

        public void setStoneMined(int stoneMined) {
            this.stoneMined = stoneMined;
        }

        public int getCoalMined() {
            return coalMined;
        }

        public void setCoalMined(int coalMined) {
            this.coalMined = coalMined;
        }
        public int getCopperMined() {
            return copperMined;
        }

        public void setCopperMined(int copperMined) {
            this.copperMined = copperMined;
        }

        public int getIronMined() {
            return ironMined;
        }

        public void setIronMined(int ironMined) {
            this.ironMined = ironMined;
        }

        public int getGoldMined() {
            return goldMined;
        }

        public void setGoldMined(int goldMined) {
            this.goldMined = goldMined;
        }

        public int getDiamondMined() {
            return diamondMined;
        }

        public void setDiamondMined(int diamondMined) {
            this.diamondMined = diamondMined;
        }

        public int getEmeraldMined() {
            return emeraldMined;
        }

        public void setEmeraldMined(int emeraldMined) {
            this.emeraldMined = emeraldMined;
        }
        public int getNetheriteMined() {
            return netheriteMined;
        }

        public void setNetheriteMined(int netheriteMined) {
            this.netheriteMined = netheriteMined;
        }
    }

    private static class TopLuckInventoryHolder implements org.bukkit.inventory.InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
