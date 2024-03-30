package fr.batminecraft.kiwianticheat.automod;

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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONObject;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoModSystem implements Listener {

    public static boolean RUN = true;
    public static String[] motsGrossiers = {
            "baiser",
            "baltringue",
            "batard",
            "bâtard",
            "bâtardisé",
            "béni-oui-oui",
            "bite",
            "biyatch",
            "boloss",
            "bouffon",
            "branleur",
            "burne",
            "casse-couilles",
            "casse-pieds",
            "chatte",
            "chiasse",
            "chiure",
            "choune",
            "claqueur",
            "con",
            "conasse",
            "connard",
            "connasse",
            "connerie",
            "coucouille",
            "couillon",
            "crisse",
            "crissou",
            "cul",
            "débile",
            "débiloïde",
            "dégueulasse",
            "enculé",
            "enculée",
            "enfoiré",
            "enfoirée",
            "pd",
            "esti",
            "estie",
            "fdp",
            "glandeur",
            "gros con",
            "gros naze",
            "grosse merde",
            "lèche-cul",
            "lopette",
            "merde",
            "merdeux",
            "nécrophile",
            "niquer",
            "pédale",
            "pédé",
            "pétasse",
            "pet-de-soeur",
            "pochtron",
            "putain",
            "pute",
            "raté",
            "salope",
            "schlag",
            "schnock",
            "sucer",
            "tarlouze",
            "trouduc",
            "trou de balle",
            "trou du cul",
            "va te faire foutre",
            "nique ta mère",
            "ntm",
            "nique ta soeur",
            "suce",
            "bdh",
    };

    @EventHandler
    public static void onPlayerChat(AsyncPlayerChatEvent event) {
        if(!RUN) {
            return;
        }
        Player player = event.getPlayer();
        String message = event.getMessage();

        boolean contientMotGrossier = false;
        String detectedMot = null;
        for (String mot : motsGrossiers) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(mot) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                contientMotGrossier = true;
                detectedMot = mot;
                break;
            }
        }

        if (contientMotGrossier) {
            player.playSound(player, Sound.ENTITY_WITHER_HURT, 1f, 1f);
            event.setMessage(PluginInfos.AUTO_MOD_PREFIX + " §8| " + PluginInfos.PREFIX + "§4Ce message contient un ou plusieurs mot(s) grossiers, celui-ci a été automatiquement censuré.");
            player.sendMessage("§8---------------------------");
            player.sendMessage(" ");
            player.sendMessage(PluginInfos.AUTO_MOD_PREFIX + " §8» §cVotre dernier message comportait un ou plusieurs mot(s) grossiers.");
            player.sendMessage("§8§o§nEn cas d'erreur§8§o, présentez-vous auprès du staff avec une capture d'écran de ce message.");
            player.sendMessage("");
            player.sendMessage("§8---------------------------");


            LocalDateTime hour = LocalDateTime.now();
            String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            PluginInfos.detectionLogger.logDetect(KiwiDetectLoggerFormater.formater(new MainDataDetectionJson(decimalDate, Bukkit.getServer().getIp()), new PlayerData(player.getName(), player.getAddress().getHostString()), new DetectedData("AUTO-MODERATION-INOFFENSIVE-DETECTION", "Detected inoffensive word in this message: " + message + ", detected inoffensive word: " + detectedMot)));
            sendEmbed(player, detectedMot, message);

        }
    }

    private static void sendEmbed(Player player, String detectedMot, String message) {
        if(!PluginInfos.enableDiscordLink) {
            return;
        }
        String webhookURL = PluginInfos.discordWebHookLink;
        String hexColor = "#E67E22";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        JSONObject embed = new JSONObject()
                    .put("title", "D.Kiwi link :kiwi: » Detected inoffensive Player")
                    .put("description", "Kiwi AntiCheat have **detect inoffensive player** on " + Bukkit.getServer().getPort())
                    .put("color", decimalColor)
                    .put("thumbnail", new JSONObject().put("url", DiscordWebHookLink.warningLogoURL))
                    .put("fields", new JSONObject[]{
                            new JSONObject()
                                    .put("name", "Player :")
                                    .put("value", player.getDisplayName())
                                    .put("inline", false),
                            new JSONObject()
                                    .put("name", "Detected by Auto-Modération :")
                                    .put("value", "Detected inoffensive word in this message: " + message + ", detected inoffensive word: " + detectedMot)
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
