package fr.batminecraft.kiwianticheat.sensors;

import fr.batminecraft.kiwianticheat.Main;
import fr.batminecraft.kiwianticheat.logger.formaters.KiwiDetectLoggerFormater;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.DetectedData;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.MainDataDetectionJson;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.PlayerData;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import fr.batminecraft.kiwianticheat.webhook.discord.DiscordWebHookLink;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static fr.batminecraft.kiwianticheat.utils.PluginInfos.main;


public class FlySensor implements Listener {

    private static final Map<UUID, Integer> lastTicksMap = new HashMap<>();
    private static final Map<UUID, Double> lastYMap = new HashMap<>();
    private static final int ticksThreshold = 2;
    public static Boolean RUN = false;
    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!RUN) {
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission("kiwiac.bypass")) {
                        return;
                    }
                    if (isRisingOrStatic(player)) {
                        punish(player);
                    }
                }
            }
        }.runTaskTimer(main, 0, 10);
    }

    public static void stop() {
        RUN = false;
    }
    private static boolean isRisingOrStatic(Player player) {

        if(player.isOp() || player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR) || player.isFlying() || player.getPlayer().getPotionEffect(PotionEffectType.LEVITATION) != null || player.isGliding()) {
            lastTicksMap.remove(player.getUniqueId());
            lastYMap.remove(player.getUniqueId());
            return false;
        }



        if (player.getLocation().getBlockY() <= 1) {
            lastTicksMap.remove(player.getUniqueId());
            lastYMap.remove(player.getUniqueId());
            return false;
        }

        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (xOffset == 0 && zOffset == 0 && yOffset == 0) {
                        continue;
                    }

                    Material block = player.getLocation().add(xOffset, yOffset, zOffset).getBlock().getType();

                    if (block != Material.AIR) {
                        lastTicksMap.remove(player.getUniqueId());
                        lastYMap.remove(player.getUniqueId());
                        return false;
                    }
                }
            }
        }

        if (!lastTicksMap.containsKey(player.getUniqueId())) {
            lastTicksMap.put(player.getUniqueId(), 0);
            lastYMap.put(player.getUniqueId(), player.getLocation().getY());
            return false;
        }

        int lastTicks = lastTicksMap.get(player.getUniqueId());
        double lastY = lastYMap.get(player.getUniqueId());
        double currentY = player.getLocation().getY();

        if (currentY < lastY) {
            lastTicksMap.put(player.getUniqueId(), 0);
            lastYMap.put(player.getUniqueId(), currentY);
            return false;
        } else if (currentY > lastY && player.getActivePotionEffects().contains(PotionEffectType.LEVITATION)) {
            lastTicksMap.put(player.getUniqueId(), 0);
            lastYMap.put(player.getUniqueId(), currentY);
            return false;
        }

        if (lastTicks >= ticksThreshold) {
            lastTicksMap.put(player.getUniqueId(), 0);
            lastYMap.put(player.getUniqueId(), currentY);
            return true;
        }

        lastTicksMap.put(player.getUniqueId(), lastTicksMap.get(player.getUniqueId()) + 1);
        lastYMap.put(player.getUniqueId(), currentY);
        return false;
    }

    private static void punish(Player player) {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(decimalDate, Bukkit.getServer().getIp()), new PlayerData(player.getName(), player.getAddress().getHostString()), new DetectedData("FLY", "Detected player without gravity in the void !")));
        sendEmbed(player);
        player.kickPlayer(PluginInfos.PREFIX + "Détecté pour FLY");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                player.sendMessage(PluginInfos.PREFIX + PluginInfos.WARNING_PREFIX + "§r§7 <> §cDetected player using FLY : " + player.getDisplayName());
            }
        }
    }

    private static void sendEmbed(Player player) {
        if(!PluginInfos.enableDiscordLink) {
            return;
        }
        String webhookURL = PluginInfos.discordWebHookLink;
        String hexColor = "#E67E22";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        JSONObject embed = new JSONObject()
                .put("title", "D.Kiwi link :kiwi: » Detected suspect Player")
                .put("description", "Kiwi AntiCheat have **detect suspect player** on " + Bukkit.getServer().getPort())
                .put("color", decimalColor)
                .put("thumbnail", new JSONObject().put("url", DiscordWebHookLink.warningLogoURL))
                .put("fields", new JSONObject[]{
                        new JSONObject()
                                .put("name", "Player :")
                                .put("value", player.getDisplayName())
                                .put("inline", false),
                        new JSONObject()
                                .put("name", "Detected cheat :")
                                .put("value", "FLY » Detected player without gravity in the void !")
                                .put("inline", false),
                        new JSONObject()
                                .put("name", "Other infos :")
                                .put("value", "Player Internet Protocol Address : " + player.getAddress().getAddress().getHostAddress())
                                .put("inline", false)
                })
                .put("footer", new JSONObject()
                        .put("text", "Kiwi AntiCheat - V" + Main.instance.getDescription().getVersion())
                        .put("icon_url", "https://cdn.discordapp.com/attachments/1176206122026270781/1216029998150979584/kiwi_ac_logo.png?ex=65fee730&is=65ec7230&hm=11cc698163f2d1d01ea4a09fd5a9f0a9915dce48f25bbd28d8bbb068c3bec9da&"));

        JSONObject json = new JSONObject()
                .put("username", "WebHook Linker - Kiwi AntiCheat")
                .put("avatar_url", "https://cdn.discordapp.com/attachments/1176206122026270781/1216029998150979584/kiwi_ac_logo.png?ex=65fee730&is=65ec7230&hm=11cc698163f2d1d01ea4a09fd5a9f0a9915dce48f25bbd28d8bbb068c3bec9da&")
                .put("embeds", new JSONObject[]{embed});

        try {
            URI uri = new URIBuilder(webhookURL).build();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);
            if (httpResponse.getEntity() != null) {
                String response = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
