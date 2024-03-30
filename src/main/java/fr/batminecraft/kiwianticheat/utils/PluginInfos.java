package fr.batminecraft.kiwianticheat.utils;

import fr.batminecraft.kiwianticheat.Main;
import fr.batminecraft.kiwianticheat.logger.KiwiLogSystem;
import fr.batminecraft.kiwianticheat.logger.KiwiLogger;
import net.md_5.bungee.api.ChatColor;

public class PluginInfos {

    public static Main main = Main.instance;

    public static KiwiLogger detectionLogger;
    public static KiwiLogger topLuckLogger;
    public static KiwiLogger errorLogger;
    public static String discordWebHookLink = "";
    public static Boolean enableDiscordLink = true;

    // Final
    public static final String PREFIX = "§8[" + ChatColor.of("#58D68D") + "§lK" + ChatColor.of("#2ECC71") + "§lI" + ChatColor.of("#28B463") + "§lW"  + ChatColor.of("#1D8348") + "§lI "  + ChatColor.of("#D1F2EB") + "§lA"  + ChatColor.of("#A3E4D7") + "§lN"  + ChatColor.of("#76D7C4") + "§lT"  + ChatColor.of("#48C9B0") + "§lI"  + ChatColor.of("#1ABC9C") + "§lC"  + ChatColor.of("#17A589") + "§lH"  + ChatColor.of("#148F77") + "§lE"  + ChatColor.of("#117864") + "§lA"  + ChatColor.of("#0E6251") + "§lT§r§8] §7» ";
    public static final String DEBUG_PREFIX = "§o§l" + ChatColor.of("#F8C471") + "D" + "§o§l" + ChatColor.of("#F5B041") + "E" + "§o§l" + ChatColor.of("#F39C12") + "B" + "§o§l" + ChatColor.of("#D68910") + "U" +"§o§l" + ChatColor.of("#E67E22") + "G";
    public static final String WARNING_PREFIX = "§o§l" + ChatColor.of("#FADBD8") + "W" + "§o§l" + ChatColor.of("#F5B7B1") + "A" + "§o§l" + ChatColor.of("#F1948A") + "R" + "§o§l" + ChatColor.of("#EC7063") + "N" +"§o§l" + ChatColor.of("#E74C3C") + "I" + "§o§l" + ChatColor.of("#CB4335") + "N" + "§o§l" + ChatColor.of("#B03A2E") + "G";
    public static final String AUTO_MOD_PREFIX = "§8[" + ChatColor.of("#C0392B") + "§lA"+ ChatColor.of("#E74C3C") + "§lU"+ ChatColor.of("#CB4335") + "§lT"+ ChatColor.of("#922B21") + "§lO"+ ChatColor.of("#7F8C8D") + "§l-"+ ChatColor.of("#BB8FCE") + "§lM"+ ChatColor.of("#A569BD") + "§lO"+ ChatColor.of("#8E44AD") + "§lD"+ ChatColor.of("#D7BDE2") + "§l." + "§r§8]";

    // Refreshable
    public static String VERSION = "(?)";
    public static String NAME = "(?)";
    public static String AUTHOR = "(?)";
    public static String DESCRIPTION = "(?)";


    public static void Refresh() {
        String S_VERSION = VERSION;
        String S_NAME = NAME;
        String S_AUTHOR = AUTHOR;
        String S_DESCRIPTION = DESCRIPTION;
        try {
            VERSION = main.getDescription().getVersion();
            NAME = main.getDescription().getName();
            AUTHOR = main.getDescription().getAuthors().get(0);
            DESCRIPTION = main.getDescription().getDescription();
        } catch (Exception e) {

            main.getLogger().warning("Error : " + e.toString());
            VERSION = S_VERSION;
            NAME = S_NAME;
            AUTHOR = S_AUTHOR;
            DESCRIPTION = S_DESCRIPTION;
        }

        try {
            detectionLogger = new KiwiLogger("detections", KiwiLogSystem.KIWI_DETECT_LOGGER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            topLuckLogger = new KiwiLogger("topluck", KiwiLogSystem.KIWI_TOP_LUCK_LOGGER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            errorLogger = new KiwiLogger("error", KiwiLogSystem.KIWI_ERROR_LOGGER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
