package fr.batminecraft.kiwianticheat.report.commands;

import fr.batminecraft.kiwianticheat.report.guis.AdminGui;
import fr.batminecraft.kiwianticheat.report.guis.ReportGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.batminecraft.kiwianticheat.report.ReportEngine.RUN;

public class ReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        if(!RUN) {
            sender.sendMessage("§4Rapport désactivé !");
            return true;
        }
        Player player = (Player) sender;
        if(player.hasPermission("kiwiac.moderation")) {
            AdminGui.openReportListGUI(player);
        } else {
            player.sendMessage("§4Tu n'as pas la permission pour cela !");
            return true;
        }
        return true;
    }
}
