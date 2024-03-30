package fr.batminecraft.kiwianticheat.topluck;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.batminecraft.kiwianticheat.topluck.TopLuckSystem.RUN;

public class OpenTopLuckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            if(!RUN) {
                player.sendMessage("§4TopLuck désactivé !");
                return true;
            }
            if(!player.hasPermission("kiwiac.bypass")) {
                player.sendMessage("§4Tu n'as pas la permission pour cela !");
                return true;
            }
            TopLuckSystem.openTopLuckGUI(player);
        }
        return false;
    }
}
