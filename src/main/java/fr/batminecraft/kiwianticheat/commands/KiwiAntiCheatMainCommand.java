package fr.batminecraft.kiwianticheat.commands;

import fr.batminecraft.kiwianticheat.Main;
import fr.batminecraft.kiwianticheat.automod.AutoModSystem;
import fr.batminecraft.kiwianticheat.config.ConfigurationHandler;
import fr.batminecraft.kiwianticheat.logger.KiwiLogSystem;
import fr.batminecraft.kiwianticheat.logger.KiwiLogger;
import fr.batminecraft.kiwianticheat.report.ReportEngine;
import fr.batminecraft.kiwianticheat.report.ReportPacket;
import fr.batminecraft.kiwianticheat.sensors.*;
import fr.batminecraft.kiwianticheat.sqllink.SqlLink;
import fr.batminecraft.kiwianticheat.topluck.TopLuckSystem;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;
import fr.batminecraft.kiwianticheat.webhook.discord.DiscordWebHookLink;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class KiwiAntiCheatMainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("kiwiac.moderation")) {
            sender.sendMessage("§4Tu n'as pas la permission pour cela !");
            return true;
        }
        String invalidFormat = "§4Format de commande invalide ! Utiliser /kiwianticheat ou pseudonymes pour afficher les exemples d'utilisation.";

        if(args.length == 0) {
            sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
            sender.sendMessage(" ");
            sender.sendMessage("            " + PluginInfos.PREFIX.replaceAll(" §7» ", "") + "©");
            sender.sendMessage(" ");
            sender.sendMessage("§6" + PluginInfos.NAME + "© §7V" + PluginInfos.VERSION + "§6:");
            sender.sendMessage(" §7- §8Auteur: §c" + PluginInfos.AUTHOR);
            sender.sendMessage(" §7- §8J.Class Principale: §c" + PluginInfos.main);
            sender.sendMessage(" ");
            sender.sendMessage("§6§l§nExemple de commandes disponibles :");
            sender.sendMessage(" ");
            sender.sendMessage(" §7- §b/kiwianticheat|kiwiac|kanticheat|kac §8» §aAfficher les informations sur le plugin");
            sender.sendMessage(" §7- §b/kiwianticheat status all §8» §aStatus des détecteurs, auto-modération, topluck");
            sender.sendMessage(" §7- §b/kiwianticheat discord test-link §8» §aTester le lien discord via D.Kiwi-Link© V1");
            sender.sendMessage(" §7- §b/kiwianticheat discord disable/enable §8» §aDésactiver/activer le lien D.Kiwi-Link©");
            sender.sendMessage(" §7- §b/kiwianticheat services all-services disable/enable §8» §aDésactiver/activer tout les détecteurs et services");
            sender.sendMessage(" §7- §b/kiwianticheat services autoclick/automod/topluck... disable/enable §8» §aDésactiver/activer un détecteur ou un service");
            sender.sendMessage(" §7- §b/kiwianticheat sql test §8» §aTester le lien MySQL/MariaDB");
            sender.sendMessage(" ");
            sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
            return true;
        } if(args.length == 1) {
            sender.sendMessage(invalidFormat);
            return true;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("services")) {
                sender.sendMessage(invalidFormat);
                return true;
            } else if(args[0].equalsIgnoreCase("status")) {
                if(args[1].equalsIgnoreCase("all")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Statuts des services et détecteurs");
                    sender.sendMessage(" ");
                    sender.sendMessage("§6§lDétecteurs :");
                    sender.sendMessage(" ");
                    sender.sendMessage(" §7» §eAutoClick§7: §c" + AutoClickSensor.RUN);
                    sender.sendMessage(" §7» §eFly§7: §c" + FlySensor.RUN);
                    sender.sendMessage(" §7» §eKillAura§7: §c" + KillAuraSensor.RUN);
                    sender.sendMessage(" §7» §eNoFall§7: §c" + NoFallSensor.RUN);
                    sender.sendMessage(" §7» §eReach§7: §c" + ReachSensor.RUN);
                    sender.sendMessage(" §7» §eSpeedHack§7: §c" + SpeedHackSensor.RUN);
                    sender.sendMessage(" ");
                    sender.sendMessage("§6§lServices :");
                    sender.sendMessage(" ");
                    sender.sendMessage(" §7» §eAuto-Modération§7: §c" + AutoModSystem.RUN);
                    sender.sendMessage(" §7» §eTopLuck§7: §c" + TopLuckSystem.RUN);
                    sender.sendMessage(" §7» §eReport§7: §c" + ReportEngine.RUN);
                    sender.sendMessage(" ");
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else if(args[1].equalsIgnoreCase("sensors")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Statuts des détecteurs");
                    sender.sendMessage(" ");
                    sender.sendMessage(" §7» §eAutoClick§7: §c" + AutoClickSensor.RUN);
                    sender.sendMessage(" §7» §eFly§7: §c" + FlySensor.RUN);
                    sender.sendMessage(" §7» §eKillAura§7: §c" + KillAuraSensor.RUN);
                    sender.sendMessage(" §7» §eNoFall§7: §c" + NoFallSensor.RUN);
                    sender.sendMessage(" §7» §eReach§7: §c" + ReachSensor.RUN);
                    sender.sendMessage(" §7» §eSpeedHack§7: §c" + SpeedHackSensor.RUN);
                    sender.sendMessage(" ");
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else if(args[1].equalsIgnoreCase("automod")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Statut du service d'auto-modération");
                    sender.sendMessage(" ");
                    sender.sendMessage(" §7» §eAuto-Modération§7: §c" + AutoModSystem.RUN);
                    sender.sendMessage(" ");
                    sender.sendMessage("     §7Mots Filtrés : §c" + AutoModSystem.motsGrossiers.length + " mots");
                    sender.sendMessage(" ");
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else if(args[1].equalsIgnoreCase("topluck")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Statut du service de TopLuck");
                    sender.sendMessage(" ");
                    sender.sendMessage(" §7» §eTopLuck§7: §c" + TopLuckSystem.RUN);
                    sender.sendMessage(" ");
                    sender.sendMessage("     §7Joueurs en analyse : §c" + TopLuckSystem.playerDataMap.size() + " joueurs");
                    sender.sendMessage(" ");
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                }else if(args[1].equalsIgnoreCase("report")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Statut du service de Rapport");
                    sender.sendMessage(" ");
                    sender.sendMessage(" §7» §eReport§7: §c" + ReportEngine.RUN);
                    sender.sendMessage(" ");
                    sender.sendMessage("     §7Rapports en attente de traitement : §c" + ReportEngine.reportList.size() + " rapports");
                    sender.sendMessage(" ");
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else {
                    sender.sendMessage(invalidFormat);
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("discord")) {
                if(args[1].equalsIgnoreCase("test-link")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Essai du D.Kiwi Link");
                    sender.sendMessage(" ");
                    sender.sendMessage("     §7Statut du lien D.Kiwi Link : §c" + PluginInfos.enableDiscordLink);
                    sender.sendMessage(" §7» §eEnvoi du message de test au webhook§7: §c" + PluginInfos.discordWebHookLink);
                    sender.sendMessage(" ");
                    sender.sendMessage(" §4Envoi en cours...");
                    try{
                        sendEmbed();
                        sender.sendMessage(" §aMessage envoyé avec succès !");
                    } catch (Exception e) {
                        sender.sendMessage(" §4§lErreur lors de l'envoi :");
                        sender.sendMessage("§4" + e.getMessage());
                    }
                    sender.sendMessage(" ");
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else if(args[1].equalsIgnoreCase("disable")) {
                    Main.instance.getConfig().set("services.d-kiwilink.enabled", false);
                    Main.instance.saveConfig();
                    if(!ConfigurationHandler.reload(Main.instance)) {
                        sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                        return true;
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    PluginInfos.errorLogger.logError("Disabled D.Kiwi link service");
                    return true;
                } else if(args[1].equalsIgnoreCase("enable")) {
                    Main.instance.getConfig().set("services.d-kiwilink.enabled", true);
                    Main.instance.saveConfig();
                    if(!ConfigurationHandler.reload(Main.instance)) {
                        sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                        return true;
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    PluginInfos.errorLogger.logError("Enabled D.Kiwi link service");
                    return true;
                } else {
                    sender.sendMessage(invalidFormat);
                    return true;
                }
            }else if(args[0].equalsIgnoreCase("sql")) {
                if(args[1].equalsIgnoreCase("test")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Essai du lien MySQL/MariaDB");
                    sender.sendMessage(" ");
                    sender.sendMessage("§aEnvoi de la requette...");
                    if(SqlLink.testLink()) {
                        sender.sendMessage("§aLe lien sql fonctionne correctement !");
                    } else {
                        sender.sendMessage("§4Erreur avec le lien sql !");
                    }
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else {
                    sender.sendMessage(invalidFormat);
                    return true;
                }
            }else if(args[0].equalsIgnoreCase("config")) {
                if(args[1].equalsIgnoreCase("reload")) {
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    sender.sendMessage(" ");
                    sender.sendMessage(PluginInfos.PREFIX + " §6Rechargement de la configuartion");
                    sender.sendMessage(" ");
                    sender.sendMessage("§aRechargement en cours...");
                    if(ConfigurationHandler.reload(Main.instance)) {
                        sender.sendMessage("§aLa configuration a bien été rechargé !");
                    } else {
                        sender.sendMessage("§4Erreur lors du rechargement !");
                    }
                    sender.sendMessage("§8-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                    return true;
                } else {
                    sender.sendMessage(invalidFormat);
                    return true;
                }
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("services")) {
                Boolean enableOrDisable;
                if(args[2].equalsIgnoreCase("disable")) {
                    enableOrDisable = false;
                } else if(args[2].equalsIgnoreCase("enable")) {
                    enableOrDisable = true;
                } else {
                    sender.sendMessage(invalidFormat);
                    return true;
                }

                if(args[1].equalsIgnoreCase("all-services")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.autoclick.enabled", true);
                        Main.instance.getConfig().set("sensors.fly.enabled", true);
                        Main.instance.getConfig().set("sensors.killaura.enabled", true);
                        Main.instance.getConfig().set("sensors.nofall.enabled", true);
                        Main.instance.getConfig().set("sensors.reach.enabled", true);
                        Main.instance.getConfig().set("sensors.speedhack.enabled", true);

                        Main.instance.getConfig().set("services.automod.enabled", true);
                        Main.instance.getConfig().set("services.topluck.enabled", true);
                        Main.instance.getConfig().set("services.report.enabled", true);

                        Main.instance.saveConfig();

                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        } else {
                            PluginInfos.errorLogger.logError("Enabled all services and sensors");
                        }


                    } else {
                        Main.instance.getConfig().set("sensors.autoclick.enabled", false);
                        Main.instance.getConfig().set("sensors.fly.enabled", false);
                        Main.instance.getConfig().set("sensors.killaura.enabled", false);
                        Main.instance.getConfig().set("sensors.nofall.enabled", false);
                        Main.instance.getConfig().set("sensors.reach.enabled", false);
                        Main.instance.getConfig().set("sensors.speedhack.enabled", false);

                        Main.instance.getConfig().set("services.automod.enabled", false);
                        Main.instance.getConfig().set("services.topluck.enabled", false);
                        Main.instance.getConfig().set("services.report.enabled", false);

                        Main.instance.saveConfig();

                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        } else {
                            PluginInfos.errorLogger.logError("Disabled all services and sensors");
                        }
                    }
                    sender.sendMessage("§aLes modifications ont bien été apportées.");
                    return true;
                } else if(args[1].equalsIgnoreCase("autoclick")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.autoclick.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled autoclick sensor");
                    } else {
                        Main.instance.getConfig().set("sensors.autoclick.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled autoclick sensor");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("fly")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.fly.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled fly sensor");
                    } else {
                        Main.instance.getConfig().set("sensors.fly.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled fly sensor");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("killaura")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.killaura.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled killaura sensor");
                    } else {
                        Main.instance.getConfig().set("sensors.killaura.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled killaura sensor");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("nofall")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.nofall.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled nofall sensor");
                    } else {
                        Main.instance.getConfig().set("sensors.nofall.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled nofall sensor");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("reach")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.reach.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled reach sensor");
                    } else {
                        Main.instance.getConfig().set("sensors.reach.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled reach sensor");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("speedhack")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("sensors.speedhack.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled speedhack sensor");
                    } else {
                        Main.instance.getConfig().set("sensors.speedhack.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled speedhack sensor");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("automod")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("services.automod.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled automod service");
                    } else {
                        Main.instance.getConfig().set("services.automod.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled automod service");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");
                    return true;
                } else if(args[1].equalsIgnoreCase("topluck")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("services.topluck.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled topluck service");
                    } else {
                        Main.instance.getConfig().set("services.topluck.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled topluck service");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");

                    return true;
                } else if(args[1].equalsIgnoreCase("report")) {
                    if(enableOrDisable) {
                        Main.instance.getConfig().set("services.report.enabled", true);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Enabled report service");
                    } else {
                        Main.instance.getConfig().set("services.report.enabled", false);
                        Main.instance.saveConfig();
                        if(!ConfigurationHandler.reload(Main.instance)) {
                            sender.sendMessage("§4Erreur lors de la mise à jour de la configuration ! Regardez la console pour plus d'informations");
                            return true;
                        }
                        PluginInfos.errorLogger.logError("Disabled report service");
                    }
                    sender.sendMessage("§aLa modification a bien été apportée.");

                    return true;
                } else {
                    sender.sendMessage(invalidFormat);
                    return true;
                }
            } else {
                sender.sendMessage(invalidFormat);
                return true;
            }
        }
        return false;
    }

    private static void sendEmbed() throws URISyntaxException, IOException {
        String webhookURL = PluginInfos.discordWebHookLink;
        String hexColor = "#BB8FCE";
        int decimalColor = Integer.parseInt(hexColor.substring(1), 16);

        JSONObject embed = new JSONObject()
                .put("title", "D.Kiwi link » Essai du lien !")
                .put("description", "Message d'essai du lien **D.Kiwi link V1©**")
                .put("color", decimalColor)
                .put("thumbnail", new JSONObject().put("url", DiscordWebHookLink.infoLogoURL))
                .put("footer", new JSONObject()
                        .put("text", "Kiwi AntiCheat - V" + Main.instance.getDescription().getVersion())
                        .put("icon_url", "https://cdn.discordapp.com/attachments/1176206122026270781/1216029998150979584/kiwi_ac_logo.png?ex=65fee730&is=65ec7230&hm=11cc698163f2d1d01ea4a09fd5a9f0a9915dce48f25bbd28d8bbb068c3bec9da&"));

        JSONObject json = new JSONObject()
                .put("username", "WebHook Linker - Kiwi AntiCheat")
                .put("avatar_url", "https://cdn.discordapp.com/attachments/1176206122026270781/1216029998150979584/kiwi_ac_logo.png?ex=65fee730&is=65ec7230&hm=11cc698163f2d1d01ea4a09fd5a9f0a9915dce48f25bbd28d8bbb068c3bec9da&")
                .put("embeds", new JSONObject[]{embed});

        URI uri = new URIBuilder(webhookURL).build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));
        httpPost.setHeader("Content-Type", "application/json");
        HttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);
        if (httpResponse.getEntity() != null) {
            String response = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(response);
        }


    }
}
