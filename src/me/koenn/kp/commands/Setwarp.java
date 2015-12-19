package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.Warping;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setwarp extends HexicCommand {

    private Main main;
    private Warping warping;

    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You need to be a player to do this!");
            return;
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        warping.setWarp(args[0], args[1], p);
    }

    public Setwarp(Main main, Warping warping) {
        super("Set a warp", "/setwarp <name> <item>", "");
        this.main = main;
        this.warping = warping;
    }
}