package me.koenn.kp.commands;

import me.koenn.kp.InventoryManager;
import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp extends HexicCommand {

    private Main main;
    private InventoryManager ivm;

    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You need to be a player to do this!");
            return;
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        ivm.openWarps(p);
    }

    public Warp(Main main, InventoryManager ivm) {
        super("Warp to places", "/warp (warp)", "");
        this.main = main;
        this.ivm = ivm;
    }
}