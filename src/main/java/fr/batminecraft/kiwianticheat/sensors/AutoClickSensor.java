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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static fr.batminecraft.kiwianticheat.utils.PluginInfos.main;

public class AutoClickSensor implements Listener {
    public static Map<UUID, Integer> clicksCount = new HashMap<>();

    public static Boolean RUN = false;
    public static Double maxCPS = 13.0;

    @EventHandler
    private void onPlayerClick(PlayerInteractEvent e) {
        if(!RUN) {
            return;
        }
        Player player = e.getPlayer();
        if(player.hasPermission("kiwiac.bypass")) {
            return;
        }
        Action action = e.getAction();
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

            if(clicksCount.containsKey(player.getUniqueId())) {
                clicksCount.replace(player.getUniqueId(), clicksCount.get(player.getUniqueId()) + 1);
            } else {
                clicksCount.put(player.getUniqueId(), 1);
            }

        }
    }

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                clicksCount.forEach((uuid, count) -> {
                    if(count > maxCPS) {
                        clicksCount.replace(Bukkit.getServer().getPlayer(uuid).getUniqueId(), 0);
                        punish(Bukkit.getServer().getPlayer(uuid), count);
                    } else if(count == 0) { } else {
                        clicksCount.replace(Bukkit.getServer().getPlayer(uuid).getUniqueId(), 0);
                    }
                });
            }
        }.runTaskTimer(main, 0, 20);
        RUN = true;
    }
    public static void stop() {
        RUN = false;
    }

    public static void punish(Player player, double cps) {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(decimalDate, Bukkit.getServer().getIp()), new PlayerData(player.getName(), player.getAddress().getHostString()), new DetectedData("AUTO-CLICK", "AUTO-CLICK *or* SCAFFOLD » Detected CPS > of `" + maxCPS + "` *(AC-CURRENT-SPEED?)* : `" + cps + "` Clicks per second")));
        sendEmbed(player, cps);
        player.kickPlayer(PluginInfos.PREFIX + "Détecté pour AUTOCLICK");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                player.sendMessage(PluginInfos.PREFIX + PluginInfos.WARNING_PREFIX + "§r§7 <> §cDetected player using AUTOCLICK : " + player.getDisplayName());
            }
        }
    }

    private static void sendEmbed(Player player, Double cps) {
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
                                .put("value", "AUTO-CLICK *or* SCAFFOLD » Detected CPS > of `" + maxCPS + "` *(AC-CURRENT-SPEED?)* : `" + cps + "` Clicks per second")
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
