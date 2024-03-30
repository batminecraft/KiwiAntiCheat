package fr.batminecraft.kiwianticheat.report.commands;

import fr.batminecraft.kiwianticheat.report.guis.ReportGui;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import static fr.batminecraft.kiwianticheat.report.ReportEngine.RUN;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        if(!RUN) {
            sender.sendMessage("§4Rapport désactivé !");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("Utilisation: /report <player>");
            return true;
        }
        Player player = (Player) sender;
        Player reportedPlayer = Bukkit.getPlayer(args[0]);
        if (reportedPlayer == null || !reportedPlayer.isOnline()) {
            player.sendMessage("§4Joueur introuvable ou hors-ligne");
            return true;
        }

        if(player == reportedPlayer) {
            player.sendMessage("§4Tu ne peux pas faire un rapport sur toi-même !");
            return true;
        }
        ReportGui.openReportGUI(player, reportedPlayer);
        return true;
    }
}

