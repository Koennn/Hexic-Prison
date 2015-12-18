package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Relog extends HexicCommand {

    private Main main;


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length < 1){
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
            return;
        }
        if(args[0].equalsIgnoreCase("all")){
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
            }
        } else {
            if (main.checkPlayer(args, 0, 1, sender)) {
                Player p = Bukkit.getServer().getPlayer(args[0]);
                p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "Forced " + p.getName() + " to relog");
            } else {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
            }
        }
    }

    public Relog(Main main) {
        super("Force a player to relog", "/relog <player>", "");
        this.main = main;
    }
}