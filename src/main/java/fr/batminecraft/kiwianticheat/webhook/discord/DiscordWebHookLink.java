package fr.batminecraft.kiwianticheat.webhook.discord;

import fr.batminecraft.kiwianticheat.Main;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

public class DiscordWebHookLink {
    public static Main main = Main.instance;
    public static String webhookURL = PluginInfos.discordWebHookLink;
    public static String kacLogoURL = "https://cdn.discordapp.com/attachments/1176206122026270781/1216029998150979584/kiwi_ac_logo.png?ex=65fee730&is=65ec7230&hm=11cc698163f2d1d01ea4a09fd5a9f0a9915dce48f25bbd28d8bbb068c3bec9da&";
    public static String bm21LogoURL = "https://cdn.discordapp.com/attachments/1176206122026270781/1216031482162970757/pp_discord.png?ex=65fee892&is=65ec7392&hm=0a45bf2d7105b07267f77ce6adfbc6929cfe8dfe338e69d5b79c5aaa34b1fcb3&";
    public static String infoLogoURL = "https://cdn.discordapp.com/attachments/1176206122026270781/1216048843943051294/Info.png?ex=65fef8bd&is=65ec83bd&hm=98d57d89ca3dce31520619a1954202fb0c5eae40e6a72e4c52ebf4c0c1ffe276&";
    public static String warningLogoURL = "https://cdn.discordapp.com/attachments/1176206122026270781/1216048844199170068/Warning.png?ex=65fef8bd&is=65ec83bd&hm=a0665e9a048eb29fdbcc9626acf497f759985aa3f94564b75e241380d8d312b4&";
    public static String errorLogoURL = "https://cdn.discordapp.com/attachments/1176206122026270781/1216048843636998324/Error.png?ex=65fef8bd&is=65ec83bd&hm=b4e6bdcc16b783af406b9ecb1c69fa93cbbc86fd360cd2b892cbdca5c77bbed6&";
    public static void loading() {
        String hexColor = "#3498DB";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        JSONObject embed = new JSONObject()
                .put("title", "Kiwi Anticheat :kiwi: » Information")
                .put("description", "Kiwi AntiCheat is **enabled** on " + Bukkit.getServer().getPort())
                .put("color", decimalColor)
                .put("thumbnail", new JSONObject().put("url", infoLogoURL))
                .put("footer", new JSONObject()
                        .put("text", "Kiwi AntiCheat - V" + main.getDescription().getVersion())
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

    public static void disabling() {
        String hexColor = "#3498DB";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        JSONObject embed = new JSONObject()
                .put("title", "Kiwi Anticheat :kiwi: » Information")
                .put("description", "Kiwi AntiCheat is **disabled** on " + Bukkit.getServer().getPort())
                .put("color", decimalColor)
                .put("thumbnail", new JSONObject().put("url", infoLogoURL))
                .put("footer", new JSONObject()
                        .put("text", "Kiwi AntiCheat - V" + main.getDescription().getVersion())
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
