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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class NoFallSensor implements Listener {
    private HashMap<UUID, Double> startY = new HashMap<>();
    private HashMap<UUID, Double> startHealth = new HashMap<>();


    public static boolean RUN = false;
    public static double fallDistanceLimit = 4.0;
    public static Main main;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!RUN) {
            return;
        }
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if(player.hasPermission("kiwiac.bypass")) {
            return;
        }

        double currentY = event.getTo().getY();
        double currentHealth = event.getPlayer().getHealth();

        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (event.getFrom().getY() > currentY) {
            if (!startY.containsKey(playerId)) {
                startY.put(playerId, currentY);
            }
            if (!startHealth.containsKey(playerId)) {
                startHealth.put(playerId, currentHealth);
            }
        } else {
            if (startY.containsKey(playerId) && startHealth.containsKey(playerId)) {
                double startHeight = startY.get(playerId);
                double startLife = startHealth.get(playerId);
                double fallDistance = startHeight - currentY;

                if (fallDistance > fallDistanceLimit ) {
                    if (currentHealth >= startLife) {
                        punish(player, fallDistance, startLife, currentHealth);
                    }
                }
                startHealth.remove(playerId);
                startY.remove(playerId);
            }
        }
    }


    public static void start() {
        RUN = true;
    }

    public static void stop() {
        RUN = false;
    }

    public static void punish(Player player, double fallDistance, double lastHealth, double currentHealth) {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(decimalDate, Bukkit.getServer().getIp()), new PlayerData(player.getName(), player.getAddress().getHostString()), new DetectedData("NO-FALL", "Detected fall distance without damage *(NF-DIST)* : `" + fallDistance + " blocks`, Dist limit : `" + fallDistanceLimit + " blocks` & Player not received damage : (last health: `" + lastHealth + "`, current health: `" + currentHealth + "`)")));
        sendEmbed(player, fallDistance, lastHealth, currentHealth);
        player.kickPlayer(PluginInfos.PREFIX + "Détecté pour NOFALL");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                player.sendMessage(PluginInfos.PREFIX + PluginInfos.WARNING_PREFIX + "§r§7 <> §cDetected player using NOFALL : " + player.getDisplayName() + " | NOFALL Dist : " + fallDistance + " for limit " + fallDistanceLimit + " + the player did not receive any fall damage (last health: `" + lastHealth + "`, current health: `" + currentHealth + "`)");
            }
        }
    }

    private static void sendEmbed(Player player, double fallDistance, double lastHealth, double currentHealth) {
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
                                .put("value", "NO-FALL » Detected fall distance without damage *(NF-DIST)* : `" + fallDistance + " blocks`, Dist limit : `" + fallDistanceLimit + " blocks` & Player not received damage : (last health: `" + lastHealth + "`, current health: `" + currentHealth + "`)")
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
