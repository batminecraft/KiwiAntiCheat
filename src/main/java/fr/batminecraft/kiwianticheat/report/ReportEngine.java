package fr.batminecraft.kiwianticheat.report;

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
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ReportEngine {

    public static List<ReportPacket> reportList = new ArrayList<>();
    public static boolean RUN = true;
    public static void report(ReportPacket packet) {
        reportList.add(packet);
        sendEmbed(packet);
        Player reporter = Bukkit.getServer().getPlayer(packet.getReporter());
        Player reported = Bukkit.getServer().getPlayer(packet.getReported());
        PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(packet.getDate(), Bukkit.getServer().getIp()), new PlayerData(reported.getName(), reported.getAddress().getHostString()), new DetectedData("REPORTED_" + packet.getType().toUpperCase(), "This player (" + reported.getName() + ") was reported by " + reporter.getName() + " for " + packet.getType())));
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("kiwiac.moderation")) {
                p.sendMessage(PluginInfos.WARNING_PREFIX + " §7§l- " + PluginInfos.PREFIX + "§cThe player §6" + reporter.getName() + " §chave reported the player §6" + reported.getName() + " §cfor §6" + packet.getType() + " §c!");
            }
        }
    }


    private static void sendEmbed(ReportPacket packet) {
        if(!PluginInfos.enableDiscordLink) {
            return;
        }
        String webhookURL = PluginInfos.discordWebHookLink;
        String hexColor = "#48C9B0";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        Player reporter = Bukkit.getServer().getPlayer(packet.getReporter());
        Player reported = Bukkit.getServer().getPlayer(packet.getReported());

        JSONObject embed = new JSONObject()
                .put("title", "D.Kiwi link » New player report !")
                .put("description", reporter.getName() + " have reported the player " + reported.getName() + " for " + packet.getType())
                .put("color", decimalColor)
                .put("thumbnail", new JSONObject().put("url", DiscordWebHookLink.warningLogoURL))
                .put("fields", new JSONObject[]{
                        new JSONObject()
                                .put("name", "Reporter :")
                                .put("value", "Name: " + reporter.getName() + ", Location: `" + reporter.getLocation().toString() + "`")
                                .put("inline", false),
                        new JSONObject()
                                .put("name", "Reported :")
                                .put("value", "Name: " + reported.getName() + ", Location: `" + reported.getLocation().toString() + "`")
                                .put("inline", false),
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
