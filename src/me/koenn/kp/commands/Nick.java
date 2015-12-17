package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nick extends CommandHandler {

    private Main main;

    public void executeNick(CommandSender sender, Command cmd, String[] args) {
        Player s = (Player) sender;
        if (main.checkPlayer(args, 0, 2, sender)) {
            Player p = Bukkit.getServer().getPlayer(args[0]);
            main.getConfig().getConfigurationSection("nicknames").createSection(p.getName());
            main.getConfig().getConfigurationSection("nicknames").set(p.getName(), args[1]);
            p.setDisplayName(args[0]);
            p.sendMessage(ChatColor.GREEN + "Set" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.GREEN + "'s nick to" + ChatColor.YELLOW + " " + ChatColor.BOLD + args[1]);
        } else {
            if (args.length == 1) {
                Player p = (Player) sender;
                main.getConfig().getConfigurationSection("nicknames").createSection(p.getName());
                main.getConfig().getConfigurationSection("nicknames").set(p.getName(), args[0]);
                p.setDisplayName(args[0]);
                p.sendMessage(ChatColor.GREEN + "Set" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + "'s " + ChatColor.GREEN + "nick to" + ChatColor.YELLOW + " " + ChatColor.BOLD + args[0]);
            } else {
                s.sendMessage(ChatColor.RED + cmd.getUsage());
            }
        }
    }
}