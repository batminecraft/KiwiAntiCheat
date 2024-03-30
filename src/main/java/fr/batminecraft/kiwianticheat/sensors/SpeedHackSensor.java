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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.json.JSONObject;

import java.net.URI;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedHackSensor implements Listener {
    public static Boolean RUN = false;
    private Map<UUID, Location> previousLocations = new HashMap<>();
    private Map<UUID, Double> totalDistances = new HashMap<>();
    private int tickCounter = 0;
    private int ticksToCheck = 20; // Nombre de ticks à surveiller
    private double speedThreshold = 25;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!RUN) {
            return;
        }
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Location currentLocation = event.getTo();

        if(player.isFlying() || !player.isOnGround() || player.isInWater() || player.isInsideVehicle() || player.isSwimming()) {
            return;
        }

        Location previousLocation = previousLocations.getOrDefault(playerId, null);
        if (previousLocation != null) {
            double distanceXZ = getDistanceXZ(previousLocation, currentLocation);
            double totalDistance = totalDistances.getOrDefault(playerId, 0.0);
            totalDistance += distanceXZ;
            totalDistances.put(playerId, totalDistance);

            if (++tickCounter >= ticksToCheck) {
                double averageSpeed = totalDistance / ticksToCheck * 20;
                if (averageSpeed > speedThreshold) {
                    punish(player, averageSpeed);

                }

                tickCounter = 0;
                totalDistances.put(playerId, 0.0);
            }
        }
        previousLocations.put(playerId, currentLocation.clone());
    }

    private double getDistanceXZ(Location loc1, Location loc2) {
        double dx = loc2.getX() - loc1.getX();
        double dz = loc2.getZ() - loc1.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }
    private static void punish(Player player, Double speed) {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(decimalDate, Bukkit.getServer().getIp()), new PlayerData(player.getName(), player.getAddress().getHostString()), new DetectedData("SPEED-HACK", "Detected invalid speed *(AVG SPEED)* : `" + speed)));
        sendEmbed(player, speed);
        player.kickPlayer(PluginInfos.PREFIX + "Détecté pour SPEED-HACK");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                DecimalFormat df = new DecimalFormat("0.0");
                p.sendMessage(PluginInfos.PREFIX + PluginInfos.WARNING_PREFIX + "§r§7 <> §cDetected player using SPEED-HACK : " + player.getDisplayName() + " | AVG SPEED : " + speed);
            }
        }
    }

    public static void start() {
        RUN = true;
    }

    public static void stop() {
        RUN = false;
    }

    private static void sendEmbed(Player player, Double angle) {
        String webhookURL = PluginInfos.discordWebHookLink;
        String hexColor = "#E67E22";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        if(!PluginInfos.enableDiscordLink) {
            return;
        }

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
                                .put("value", "SPEED-HACK » Detected invalid speed *(AVG SPEED)* : `" + angle)
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
