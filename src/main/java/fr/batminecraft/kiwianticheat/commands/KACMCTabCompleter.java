package fr.batminecraft.kiwianticheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class KACMCTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("services");
            completions.add("status");
            completions.add("discord");
            completions.add("sql");
            completions.add("config");

            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], completions, suggestions);
            return suggestions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("services")) {
            List<String> completions = new ArrayList<>();
            completions.add("all-services");
            completions.add("autoclick");
            completions.add("fly");
            completions.add("killaura");
            completions.add("nofall");
            completions.add("reach");
            completions.add("speedhack");
            completions.add("automod");
            completions.add("topluck");

            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], completions, suggestions);
            return suggestions;
        }else if (args.length == 2 && args[0].equalsIgnoreCase("sql")) {
            List<String> completions = new ArrayList<>();
            completions.add("test");

            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], completions, suggestions);
            return suggestions;
        }else if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
            List<String> completions = new ArrayList<>();
            completions.add("reload");

            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], completions, suggestions);
            return suggestions;
        }else if (args.length == 2 && args[0].equalsIgnoreCase("status")) {
            List<String> completions = new ArrayList<>();
            completions.add("all");
            completions.add("sensors");
            completions.add("automod");
            completions.add("topluck");
            completions.add("report");


            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], completions, suggestions);
            return suggestions;
        }else if (args.length == 2 && args[0].equalsIgnoreCase("discord")) {
            List<String> completions = new ArrayList<>();
            completions.add("test-link");
            completions.add("disable");
            completions.add("enable");

            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], completions, suggestions);
            return suggestions;
        }else if (args.length == 3 && args[0].equalsIgnoreCase("services") && (args[1].equalsIgnoreCase("all-services") || args[1].equalsIgnoreCase("autoclick") || args[1].equalsIgnoreCase("killaura") || args[1].equalsIgnoreCase("nofall") || args[1].equalsIgnoreCase("reach") || args[1].equalsIgnoreCase("speedhack") || args[1].equalsIgnoreCase("automod") || args[1].equalsIgnoreCase("topluck"))) {
            List<String> completions = new ArrayList<>();
            completions.add("disable");
            completions.add("enable");

            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[2], completions, suggestions);
            return suggestions;
        }

        return null;
    }
}
