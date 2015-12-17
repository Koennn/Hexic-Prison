package me.koenn.kp.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gotoserver extends HexicCommand {

    private Main main;


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (main.checkPlayer(args, 0, 2, sender)){
            Player p = Bukkit.getServer().getPlayer(args[0]);
            p.closeInventory();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + p.getName() + " " + args[1]);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "Welcome to the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Hexic " + args[1] + ChatColor.GREEN + " Server!"),20);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "Welcome to the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Hexic " + args[1] + ChatColor.GREEN + " Server!"),60);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "Welcome to the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Hexic " + args[1] + ChatColor.GREEN + " Server!"),50);
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }

    public Gotoserver(Main main) {
        super("Go to a different server", "/gotoserver <player> <server>", "/gts");
        this.main = main;
    }
}