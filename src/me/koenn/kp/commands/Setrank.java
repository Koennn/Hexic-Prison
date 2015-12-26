package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setrank extends HexicCommand {

    private Main main;
    private Ranks ranks;

    public Setrank(Main main, Ranks ranks) {
        super("Set a player's rank", "/setrank <player> <rank>", "");
        this.main = main;
        this.ranks = ranks;
    }

    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        if(!(main.checkPlayer(args, 0, 2, sender))){
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
            return;
        }
        Player p = Bukkit.getPlayer(args[0]);
        try{
            ranks.setRank(p, Ranks.PrisonRank.valueOf(args[1].toUpperCase()));
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Set " + p.getName() + "'s rank to " + args[1]);
        }catch (Exception e){
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "Unknown rank '" + args[1] + "'");
        }
    }
}