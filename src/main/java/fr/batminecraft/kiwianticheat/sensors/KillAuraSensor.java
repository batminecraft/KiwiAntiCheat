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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import java.net.URI;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KillAuraSensor implements Listener {

    public static Boolean RUN = false;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!RUN) {
            return;
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player targetPlayer = (Player) event.getEntity();

            if(attacker.hasPermission("kiwiac.bypass")) {
                return;
            }
            if (checkKillAura(attacker, targetPlayer)) {
                double angleBetweenPlayers = calculateAngleBetweenPlayers(attacker, targetPlayer);
                punish(attacker, angleBetweenPlayers);
            }
        }
    }

    public static boolean checkKillAura(Player attacker, Player targetPlayer) {
        Location attackerEye = attacker.getEyeLocation();
        Vector attackerDirection = attackerEye.getDirection().normalize();
        Location targetLocation = targetPlayer.getEyeLocation();

        // Lancer un rayon en direction du joueur frappé
        Entity hitEntity = performRaycast(attackerEye, attackerDirection, targetLocation);
        return hitEntity != null && hitEntity.equals(targetPlayer);
    }

    private static Entity performRaycast(Location origin, Vector direction, Location targetLocation) {
        double maxDistance = origin.distance(targetLocation); // Distance maximale du rayon, la distance entre les deux joueurs
        Location currentLocation = origin.clone();
        World world = origin.getWorld();

        while (currentLocation.distance(origin) < maxDistance) {
            currentLocation.add(direction);

            // Vérifie si le rayon touche la hitbox du joueur cible
            for (Entity entity : world.getNearbyEntities(currentLocation, 0.5, 0.5, 0.5)) {
                if (entity instanceof Player && entity.equals(targetLocation)) {
                    return entity;
                }
            }
        }

        return null;
    }

    public static double calculateAngleBetweenPlayers(Player player1, Player player2) {
        Vector directionToPlayer1 = player1.getLocation().toVector().subtract(player2.getLocation().toVector()).normalize();
        Vector directionToPlayer2 = player2.getLocation().toVector().subtract(player1.getLocation().toVector()).normalize();
        return Math.toDegrees(Math.acos(directionToPlayer1.dot(directionToPlayer2)));
    }



    private static void punish(Player player, Double angle) {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(decimalDate, Bukkit.getServer().getIp()), new PlayerData(player.getName(), player.getAddress().getHostString()), new DetectedData("KILL-AURA", "Detected invalid FOV angle (FOV: " + angle.toString())));
        sendEmbed(player, angle);
        player.kickPlayer(PluginInfos.PREFIX + "Détecté pour KILL-AURA");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                DecimalFormat df = new DecimalFormat("0.0");
                p.sendMessage(PluginInfos.PREFIX + PluginInfos.WARNING_PREFIX + "§r§7 <> §cDetected player using KILL-AURA : " + player.getDisplayName());
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
                                .put("value", "KILL-AURA » Detected invalid FOV angle (FOV: " + angle.toString())
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
