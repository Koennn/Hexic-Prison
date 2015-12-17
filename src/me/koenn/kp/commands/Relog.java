package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Relog extends CommandHandler {

    private Main main;

    public void executeRelog(CommandSender sender, Command cmd, String[] args) {
        if(args[0].equalsIgnoreCase("all")){
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
            }
        } else {
            if (main.checkPlayer(args, 0, 1, sender)) {
                Player p = Bukkit.getServer().getPlayer(args[0]);
                p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
            } else {
                sender.sendMessage(ChatColor.RED + cmd.getUsage());
            }
        }
    }
}